package com.example.lqresier.filedialoglibrary;

/**
 * Created by Administrator on 2018/2/7.
 */

public class FileInfo {
    private String fileName;
    private String fileSize;
    private String fileTime;
    private boolean isFile;//如果isFile为true，则为文件；否则为目录

    public FileInfo(String fileName, String fileSize, String fileTime, Boolean isFile){
        this.fileName=fileName;
        this.fileSize=fileSize;
        this.fileTime=fileTime;
        this.isFile=isFile;
    }

    public void setFileName(String fileName){
        this.fileName=fileName;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFileTime() {
        return fileTime;
    }

    public boolean isFile(){
        return isFile;
    }
}
