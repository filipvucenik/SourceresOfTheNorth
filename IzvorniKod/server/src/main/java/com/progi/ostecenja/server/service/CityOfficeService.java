package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.repo.CityOffice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CityOfficeService {
    List<CityOffice> listAll();

    CityOffice createCityOffice(CityOffice office);

    CityOffice fetch(Long cityOfficeId);

    CityOffice updateCityOffice(CityOffice office);

    CityOffice deleteCityOffice(long officeId);

    Optional<CityOffice> findByCityOfficeEmail(String email);
}
