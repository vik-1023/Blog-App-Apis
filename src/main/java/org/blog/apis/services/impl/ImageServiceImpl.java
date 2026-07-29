package org.blog.apis.services.impl;

import org.blog.apis.services.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${project.image}")
    private String path;

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        String randomId = UUID.randomUUID().toString();
        String newFileName = randomId + fileName.substring(fileName.lastIndexOf("."));
        String filePath = path + File.separator + newFileName;
        File destinationFile = new File(filePath);
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        Files.copy(
                file.getInputStream(),
                Paths.get(filePath)
        );

        return newFileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {

        String fullPath = path + File.separator + fileName;

        return new FileInputStream(fullPath);

    }


}
