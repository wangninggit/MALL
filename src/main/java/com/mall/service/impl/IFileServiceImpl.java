package com.mall.service.impl;

import com.google.common.collect.Lists;
import com.mall.service.IFileService;
import com.mall.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service("iFileService")
public class IFileServiceImpl implements IFileService {

    private static Logger logger = LoggerFactory.getLogger(IFileServiceImpl.class);


    public String uploadFile(MultipartFile file ,String path){
        String uploadFileName = file.getOriginalFilename();
        //截取扩展名
        String extendName = uploadFileName.substring(uploadFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString()+extendName;
        logger.info("上传文件名:{}，文件路径:{}，新文件名:{}",uploadFileName,path,newFileName);

        File fileDir = new File(path);//创建文件路径
        File targetFile = new File(path,newFileName);//创建文件
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        try {
            //上传到tomcat服务器
            file.transferTo(targetFile);
            //把targetFile文件上传到ftp服务器
            FtpUtil.upload(Lists.newArrayList(targetFile));
            //传输完成后把本地targetFile文件删除
            targetFile.delete();
        } catch (IOException e) {
            logger.error("文件上传异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
