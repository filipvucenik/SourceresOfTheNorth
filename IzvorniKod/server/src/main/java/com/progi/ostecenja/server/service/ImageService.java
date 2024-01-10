package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.repo.Image;

import java.util.List;

public interface ImageService {

    Image createImage(Image image);
    List<Image> fillImages(List<Image> images);
    List<Image> listAllId(Long reportID);
}
