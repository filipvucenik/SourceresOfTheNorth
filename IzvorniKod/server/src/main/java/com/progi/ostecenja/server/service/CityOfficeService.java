package com.progi.ostecenja.server.service;

import com.progi.ostecenja.server.repo.CityOffice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CityOfficeService {
    List<CityOffice> listAll();

    CityOffice createCityOffice(CityOffice office);

    CityOffice fetch(Long cityOfficeId);

    CityOffice updateCityOffice(CityOffice office);

    CityOffice deleteCityOffice(long officeId);
}
