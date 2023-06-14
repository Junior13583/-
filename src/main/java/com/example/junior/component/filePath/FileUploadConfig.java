package com.example.junior.component.filePath;

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
* @Description: 配置文件上传路径
* @Author: Junior
* @Date: 2023/6/14
*/
public class FileUploadConfig {
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @PostConstruct
    public void init() throws IOException {
        Path path = Paths.get(uploadPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
}
