package com.greatxcf.lease.web.admin.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    /**
     * Upload file to object storage and return its accessible url.
     *
     * @param file multipart file
     * @return url
     */
    String upload(MultipartFile file) throws IOException;
}
