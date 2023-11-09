package com.progi.ostecenja.server.dao;

import com.progi.ostecenja.server.repo.CityOffice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityOfficeRepository extends JpaRepository<CityOffice, Long> {
    Optional<CityOffice> findByCityOfficeId(Long officeID);
    int countByCityOfficeEmail(String email);
    int countByCityOfficeName(String name);

    boolean existsByCityOfficeEmailAndCityOfficeIdNot(String email, Long officeId);
    boolean existsByCityOfficeNameAndCityOfficeIdNot(String officeName, Long officeId);
}
