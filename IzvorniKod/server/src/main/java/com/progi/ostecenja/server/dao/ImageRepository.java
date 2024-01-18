package com.progi.ostecenja.server.dao;

import com.progi.ostecenja.server.repo.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findImageByReportID(Long reportId);
}
