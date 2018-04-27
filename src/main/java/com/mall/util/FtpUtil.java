package com.mall.util;


import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FtpUtil {

    private static  Logger logger= LoggerFactory.getLogger(FtpUtil.class);
    private static final String FTPIP=PropertiesUtil.getProperty("ftp.server.ip");
    private static final String USER=PropertiesUtil.getProperty("ftp.user");
    private static final String PWD=PropertiesUtil.getProperty("ftp.pass");


    public static boolean upload(List<File> fileList) throws IOException {
        //链接ftp服务器
        FtpUtil ftpUtil = new FtpUtil(FTPIP,21,USER,PWD);
        logger.info("开始链接ftp服务器");
        boolean result = ftpUtil.upload("img",fileList);
        logger.info("文件传输结束");
        return result;
    }

    private boolean upload(String remotePath, List<File> fileList) throws IOException {
        boolean uploader = true;
        FileInputStream fis = null;
        if(connect(this.ip,this.user,this.pwd)){//链接完成后开始设置参数
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for(File Item : fileList ){
                    fis = new FileInputStream(Item);//文件放入流中
                    ftpClient.storeFile(Item.getName(),fis);//文件名和文件流
                }
            } catch (IOException e) {
                logger.error("文件传输失败",e);
                uploader = false;
                e.printStackTrace();
            }finally {
                fis.close();    //流要断开
                ftpClient.disconnect();
            }
        }
        return uploader;
    }

    private boolean connect(String ip,String user,String pwd){
        boolean isSuccess = false;
            ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user,pwd);
        } catch (IOException e) {
            logger.error("链接图片服务器失败",e);
            e.printStackTrace();
        }
        return isSuccess;
    }





    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public FtpUtil(String ip, int port, String user, String pwd){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }





    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
