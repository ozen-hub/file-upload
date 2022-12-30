package com.example.fileupload.fileupload.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
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

    @Value("${bucket}")
    private String bucket;

    @Autowired
    public FileServiceImpl(AmazonS3 s3, AmazonS3Client s3Client) {
        this.s3 = s3;
        this.s3Client = s3Client;
    }


    @Override
    public HashMap<String, Object> createFile(MultipartFile file, String directory/*abc/sample/*/,
                                              String user, int randomId) throws IOException {
        String finalizedDirectory = user+"/"+directory;
        String originalFilename = file.getOriginalFilename();
        String renamedFilename = randomId+"-"+file.getOriginalFilename(); // 2344-abc.jpg
        PutObjectResult putObjectResult = s3Client.putObject(
                new PutObjectRequest(bucket, finalizedDirectory + renamedFilename, file.getInputStream(),
                         new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));
        HashMap<String, Object> hMap = new HashMap<>();
        hMap.put("hash", putObjectResult.getContentMd5());
        hMap.put("resource", s3Client.getResourceUrl(bucket, finalizedDirectory  + renamedFilename));
        hMap.put("directory", finalizedDirectory);
        hMap.put("renamedFile", renamedFilename);
        hMap.put("originalFile", originalFilename);

        return hMap;
    }

    @Override
    public boolean deleteFile(String directory, String fileName) throws IOException {
        s3Client.deleteObject(bucket,directory+fileName);
        return true;
    }

    @Override
    public byte[] downloadFile(String directory, String fileName) throws IOException {
        S3Object object = s3.getObject(bucket, directory+fileName);
        S3ObjectInputStream objectInputStream = object.getObjectContent();
        return IOUtils.toByteArray(objectInputStream);
    }

    @Override
    public ObjectListing allFiles() throws IOException {
        return  s3.listObjects(bucket);
    }
}
