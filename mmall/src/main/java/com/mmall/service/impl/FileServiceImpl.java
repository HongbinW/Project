package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: HongbinW
 * @Date: 2019/4/11 16:08
 * @Version 1.0
 * @Description:
 */
@Service("iFileService")
@Slf4j
public class FileServiceImpl implements IFileService {

//    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);   //返回从"."之后开始的拓展名
        //A: abc.jpg
        //B: abc.jpg    防止这种情况发生
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        log.info("开始上传文件，上传文件的文件名：{}，上传的路径是:{}，新文件名：{}",fileName,path,uploadFileName); //此处{}为占位符
        //创建目录
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path,uploadFileName);

        System.out.println("#############" + targetFile);
        System.out.println("-------------" + uploadFileName);
        System.out.println("*************" + path);

        try {
            file.transferTo(targetFile);
            //文件上传成功
            //将targetFile上传到我们的FTP服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到FTP服务器，删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
