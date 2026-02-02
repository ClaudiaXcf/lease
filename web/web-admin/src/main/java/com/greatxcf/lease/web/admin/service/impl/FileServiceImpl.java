package com.greatxcf.lease.web.admin.service.impl;


import com.aliyun.oss.*;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.greatxcf.lease.common.aliyunoss.ALiYunOssConfiguration;
import com.greatxcf.lease.common.aliyunoss.ALiYunOssProperties;
import com.greatxcf.lease.web.admin.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.aliyun.oss.OSSException; // 通常 ClientException 会和 OSSException 一起使用

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    // 推荐使用 OSS 接口，而非具体的实现类 OSSClient
    private final OSS ossClient;
    private final ALiYunOssProperties properties;

    // 构造器注入：确保依赖在对象创建时就已准备好，并允许使用 final
    public FileServiceImpl(OSS ossClient, ALiYunOssProperties properties) {
        this.ossClient = ossClient;
        this.properties = properties;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }

        // 0. 获取原始文件名
        String originalFilename = file.getOriginalFilename();

        // 文件名不能为空
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new RuntimeException("文件名不能为空");
        }

        // 1. 生成唯一文件名 (objectName)
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = UUID.randomUUID().toString() + extension;

        // 2. 获取 Bucket 名称
        String bucketName = properties.getBucketName();

        // **3. 核心：构造公网 URL 前缀**
        // 格式通常是: https://[bucketName].[endpoint]/
        // 注意：这里假设您的 Bucket 名称不包含 'https://' 或 'http://'
        String urlPrefix = "https://" + bucketName + "." + properties.getEndpoint() + "/";

        InputStream inputStream = file.getInputStream();

        // 4. 创建 PutObjectRequest 对象
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);

        // 5. 执行上传
        ossClient.putObject(putObjectRequest);

        // 6. 拼接完整的公网 URL 并返回
        return urlPrefix + objectName;
    }
}

