package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: HongbinW
 * @Date: 2019/4/11 16:08
 * @Version 1.0
 * @Description:
 */
public interface IFileService {
    String upload(MultipartFile file, String path);
}
