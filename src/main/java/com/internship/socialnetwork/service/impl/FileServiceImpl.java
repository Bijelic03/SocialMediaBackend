package com.internship.socialnetwork.service.impl;

import com.internship.socialnetwork.config.AppConfig;
import com.internship.socialnetwork.service.FileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final AppConfig appConfig;

    @Override
    public String uploadFile(MultipartFile file, String path) {
        String filePath = path + file.getOriginalFilename();
        try {
            appConfig.minioClient().putObject(PutObjectArgs.builder()
                    .bucket("media-files")
                    .object(filePath)
                    .stream(file.getInputStream(), file.getSize(), appConfig.getFILE_PART_SIZE())
                    .contentType(file.getContentType())
                    .build());
            return filePath;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}