package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.service.IStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

@Service
public class StorageService implements IStorageService {
    private static long counter = 1;
    @Override
    public String saveImage(MultipartFile file) throws IOException {
        Path path = Path.of("./src/main/resources/static/images");
        String extension = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[1];
        String name = String.valueOf(counter++) + "." + extension;
        Files.createDirectories(path);
        Files.write(Path.of(path +"/"+ name), file.getBytes());
        return "./images"+name;
    }
}
