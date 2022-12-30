package com.example.fileupload.fileupload.api;

import com.example.fileupload.fileupload.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/create", params = {"directory", "user"})
    public HashMap<String, Object> createFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam String directory,
            @RequestParam String user
    ) throws IOException {
        return fileService.createFile(file, directory, user, new Random().nextInt(1001));
    }

    @DeleteMapping(value = "/remove", params = {"directory", "fileName"})
    public boolean deleteFile(
            @RequestParam String directory,
            @RequestParam String fileName) throws IOException {
        return fileService.deleteFile(directory, fileName);
    }
}
