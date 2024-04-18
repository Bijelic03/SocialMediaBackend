package com.internship.socialnetwork.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {


    String uploadFile(MultipartFile file, String path);
}
