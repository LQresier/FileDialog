package com.example.lqresier.filedialoglibrary;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/28.
 */

public class FileDialog extends AppCompatDialog implements DialogInterface{

    public FileDialog(Context context){
        super(context);
    }

    public FileDialog(Context context, int themeResId){
        super(context,themeResId);
    }

    /*
    *接口:selectListener
    */
    public interface SelectListener{
        public void onFinishedSelect(boolean isSuccess, File targetFile);
    }

    public static class Builder{
        private Context context;
        private String title;
        private String negativeButtonText;
        private RecyclerView recyclerView;
        private View layout;
        private List<FileInfo> fileList;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        final private String rootPath="/sdcard";
        private SelectListener mSelectListener;
        private FileDialog mFileDialog;

        public Builder(Context context){
            this.context=context;
        }

//        public Builder setTitle(String title){
//            this.title=title;
//            return this;
//        }
//
//        /*
//        通过resource来设置title
//         */
//        public Builder setTitle(int title){
//            this.title=(String)context.getText(title);
//            return this;
//        }

        public void setSelectListener(SelectListener mSelectListener){
            this.mSelectListener=mSelectListener;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener negativeButtonClickListener){
            this.negativeButtonText=negativeButtonText;
            this.negativeButtonClickListener=negativeButtonClickListener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener negativeButtonClickListener){
            this.negativeButtonText=(String)context.getText(negativeButtonText);
            this.negativeButtonClickListener=negativeButtonClickListener;
            return this;
        }

        private void loadFileList(){
            fileList=new ArrayList<FileInfo>();
            File file=new File(rootPath);
            File[] files=file.listFiles();
            if(files!=null && files.length>0){
                for(int i=0;i<files.length;i++){
                    if(files[i].isFile()){
                        FileInfo fileInfo=new FileInfo(files[i].getName(),"文件","",true);
                        fileList.add(fileInfo);
                    }else{
                        FileInfo fileInfo=new FileInfo(files[i].getName(),"文件夹","",false);
                        fileList.add(fileInfo);
                    }
                }
            }
        }

        public FileDialog create(){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final FileDialog dialog=new FileDialog(context, R.style.commonDialog);
            layout=inflater.inflate(R.layout.dialog_common,null);
            dialog.addContentView(layout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT));
            ((TextView)layout.findViewById(R.id.title)).setText(rootPath);
            if(negativeButtonText!=null){
                ((TextView)layout.findViewById(R.id.cancel)).setText(negativeButtonText);
                if(negativeButtonClickListener!=null){
                    ((TextView)layout.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog,BUTTON_NEGATIVE);
                        }
                    });
                }
            }
            loadFileList();
            if(fileList!=null && fileList.size()>0){
                recyclerView=(RecyclerView) layout.findViewById(R.id.list_file);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                FileAdapter adapter=new FileAdapter(fileList, this, rootPath, new FileAdapter.ClickFileListener() {
                    @Override
                    public void onClickFile(boolean isSuccess, File targetFile) {
                        mSelectListener.onFinishedSelect(isSuccess,targetFile);
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            return dialog;
        }

        /*
        *updateFileList()
        * 当点击进入下一级文件（目录）或返回上一级，更新文件列表
        * 参数：(1)nextPath:将要跳转的路径
         */
        public void updateFileList(final String nextPath){
            fileList=new ArrayList<FileInfo>();
            File file=new File(nextPath);
            File[] files=file.listFiles();
            fileList.clear();
            if(files!=null && files.length>0){
                for(int i=0;i<files.length;i++){
                    if(files[i].isFile()){
                        FileInfo fileInfo=new FileInfo(files[i].getName(),"文件","",true);
                        fileList.add(fileInfo);
                    }else{
                        FileInfo fileInfo=new FileInfo(files[i].getName(),"文件夹","",false);
                        fileList.add(fileInfo);
                    }
                }
            }
            FileAdapter adapter=new FileAdapter(fileList, this, nextPath, new FileAdapter.ClickFileListener() {
                @Override
                public void onClickFile(boolean isSuccess, File targetFile) {
                    mSelectListener.onFinishedSelect(isSuccess,targetFile);
                }
            });
            ((TextView)layout.findViewById(R.id.title)).setText(nextPath);
            recyclerView.setAdapter(adapter);
            ImageView backButton=(ImageView)layout.findViewById(R.id.back);
            if(!nextPath.equals(rootPath)){
                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] fileNames=nextPath.split("/");
                        String newPath="";
                        String logPath="";
                        for(int i=1;i<fileNames.length-1;i++){
                            String tempLog=" ["+fileNames[i]+"] ";
                            logPath+=tempLog;
                            newPath+="/";
                            newPath+=fileNames[i];
                        }
                        Log.d("FileDialog,logPath", "onClick: "+logPath);
                        updateFileList(newPath);
                    }
                });
            }else{
                backButton.setVisibility(View.INVISIBLE);
            }
        }

        public FileDialog show(){
            mFileDialog= create();
            mFileDialog.show();
            return mFileDialog;
        }

        public void dismiss(){
            mFileDialog.dismiss();
        }
    }
}
