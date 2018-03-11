package com.example.lqresier.filedialoglibrary;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/2/7.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder>{
    private List<FileInfo> mFileList;
    private FileDialog.Builder mFileDialog;
    private String nowPath;
    private ClickFileListener mClickFileListener;

    public interface ClickFileListener{
        public void onClickFile(boolean isSuccess, File targetFile);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View fileView;
        ImageView fileImage;
        TextView fileName;
        TextView fileSize;
        TextView fileTime;

        public ViewHolder(View view){
            super(view);
            fileView=view;
            fileImage=(ImageView)view.findViewById(R.id.image_file);
            fileName=(TextView)view.findViewById(R.id.name_file);
            fileSize=(TextView)view.findViewById(R.id.size_file);
            fileTime=(TextView)view.findViewById(R.id.time_file);
        }
    }

    public FileAdapter(List<FileInfo> fileList,FileDialog.Builder fileDialog,String nowPath,ClickFileListener mClickFileListener){
        mFileList=fileList;
        mFileDialog=fileDialog;
        this.nowPath=nowPath;
        this.mClickFileListener=mClickFileListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.fileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                FileInfo fileInfo=mFileList.get(position);
                if(!fileInfo.isFile()){
                    String nextPath=nowPath+"/"+fileInfo.getFileName();
                    mFileDialog.updateFileList(nextPath);
                }else{
                    String filePath=nowPath+"/"+fileInfo.getFileName();
                    final File targetFile=new File(filePath);
                    AlertDialog.Builder dialog=new AlertDialog.Builder(parent.getContext());
                    dialog.setTitle("提示");
                    dialog.setMessage("是否选择此文件");
                    dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mClickFileListener.onClickFile(true,targetFile);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        FileInfo file=mFileList.get(position);
        if(file.isFile()){
            holder.fileImage.setImageResource(R.drawable.ic_file);
        }else{
            holder.fileImage.setImageResource(R.drawable.ic_dir);
        }
        holder.fileName.setText(file.getFileName());
        holder.fileSize.setText(file.getFileSize());
        holder.fileTime.setText(file.getFileTime());
    }

    @Override
    public int getItemCount(){
        return mFileList.size();
    }
}
