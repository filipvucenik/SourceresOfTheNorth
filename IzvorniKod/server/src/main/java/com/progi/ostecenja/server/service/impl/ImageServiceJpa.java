package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.ImageRepository;
import com.progi.ostecenja.server.repo.Image;
import com.progi.ostecenja.server.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceJpa implements ImageService {
    @Autowired
    ImageRepository imageRepo;

    @Override
    public Image createImage(Image image) {
        return imageRepo.save(image);
    }

    @Override
    public List<Image> fillImages(List<Image> images) {
        return imageRepo.saveAll(images);
    }

    @Override
    public List<Image> listAllId(Long reportID) {
        return imageRepo.findAll().stream().filter(im -> im.getReportID().equals(reportID)).toList();
    }
}
