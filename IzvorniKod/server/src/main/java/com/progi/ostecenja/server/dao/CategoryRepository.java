package com.progi.ostecenja.server.dao;

import com.progi.ostecenja.server.repo.Category;
import com.progi.ostecenja.server.repo.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
}
