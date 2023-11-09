package com.progi.ostecenja.server.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IStorageService {
    String saveImage(MultipartFile file) throws IOException;
}
