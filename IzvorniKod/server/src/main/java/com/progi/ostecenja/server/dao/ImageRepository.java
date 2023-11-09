package com.progi.ostecenja.server.dao;

import com.progi.ostecenja.server.repo.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
