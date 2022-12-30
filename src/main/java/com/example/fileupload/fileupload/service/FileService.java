package com.example.fileupload.fileupload.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

public interface FileService {
    public HashMap<String,Object> createFile(MultipartFile file, String directory, String user, int randomId) throws IOException;
    public boolean deleteFile(String directory, String fileName) throws IOException;
}
