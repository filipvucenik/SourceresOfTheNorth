package com.progi.ostecenja.server.controler;

import com.progi.ostecenja.server.repo.CityOffice;
import com.progi.ostecenja.server.service.CityOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/office")
public class OfficeController {
    @Autowired
    private CityOfficeService cityOfficeService;

    @GetMapping
    public List<CityOffice> listOffices(){
        return cityOfficeService.listAll();
    }

    @PostMapping
    public CityOffice createOffice(@RequestBody CityOffice office){ return cityOfficeService.createCityOffice(office); }

    @GetMapping("/{cityOfficeId}")
    public CityOffice getCityOffice(@PathVariable("cityOfficeId") long cityOfficeId){
        return cityOfficeService.fetch(cityOfficeId);
    }

    @PutMapping("/{cityOfficeId}")
    public CityOffice updateCityOffice(@PathVariable("cityOfficeId") long cityOfficeId, @RequestBody CityOffice cityOffice){
        if(cityOffice.getCityOfficeId()==null)
            throw  new IllegalArgumentException("City Office ID missing");
        if(!cityOffice.getCityOfficeId().equals(cityOfficeId))
            throw new IllegalArgumentException("City Office ID must be preserved");
        return cityOfficeService.updateCityOffice(cityOffice);
    }



    @DeleteMapping("/{cityOfficeId}")
    public CityOffice deleteCityOffice(@PathVariable("cityOfficeId") long officeId){
        return cityOfficeService.deleteCityOffice(officeId);
    }
}
