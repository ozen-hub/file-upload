package com.example.fileupload.fileupload.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.fileupload.fileupload.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@Service
public class FileServiceImpl implements FileService {

    private final AmazonS3 s3;
    private final AmazonS3Client s3Client;

    @Value("bucket")
    private String bucket;

    @Autowired
    public FileServiceImpl(AmazonS3 s3, AmazonS3Client s3Client) {
        this.s3 = s3;
        this.s3Client = s3Client;
    }


    @Override
    public HashMap<String, Object> createFile(MultipartFile file) throws IOException {
        String directory = "sample/resource";

        String originalFilename = file.getOriginalFilename();
        PutObjectResult putObjectResult = s3Client.putObject(
                new PutObjectRequest(bucket, directory + "/" + originalFilename, file.getInputStream(),
                        new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));
        HashMap<String, Object> hMap = new HashMap<>();
        hMap.put("hash", putObjectResult.getContentMd5());
        hMap.put("resource", s3Client.getResourceUrl(bucket, directory + "/" + originalFilename));

        return hMap;
    }
}
