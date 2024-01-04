package com.progi.ostecenja.server.service.impl;

import com.progi.ostecenja.server.dao.CityOfficeRepository;
import com.progi.ostecenja.server.repo.CityOffice;
import com.progi.ostecenja.server.service.CityOfficeService;
import com.progi.ostecenja.server.service.EntityMissingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class CityOfficeServiceJpa implements CityOfficeService {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Autowired
    private CityOfficeRepository cityOfficeRepo;
    private BCryptPasswordEncoder pswdEncoder = new BCryptPasswordEncoder();

    @Override
    public List<CityOffice> listAll() {
        return cityOfficeRepo.findAll();
    }

    @Override
    public CityOffice fetch(Long officeId){
        return cityOfficeRepo.findByCityOfficeId(officeId).orElseThrow(
                () -> new EntityMissingException(CityOffice.class, officeId)
        );
    }

    public void funk(HttpSession session){
        String userId = (String) session.getAttribute("USER");
    }
    @Override
    public Optional<CityOffice> findByCityOfficeEmail(String email) {
        return cityOfficeRepo.findByCityOfficeEmail(email);
    }

    @Override
    public CityOffice createCityOffice(CityOffice cityOffice){
        validate(cityOffice);

        //Id validation
        Assert.isNull(cityOffice.getCityOfficeId(),"City Office Id must be null, not "+cityOffice.getCityOfficeId());

        //Unique email
        if(cityOfficeRepo.countByCityOfficeEmail(cityOffice.getCityOfficeEmail()) > 0){
            throw new IllegalArgumentException("City Office with email "+cityOffice.getCityOfficeEmail() + " already exists");
        }

        //Unique name
        if(cityOfficeRepo.countByCityOfficeName(cityOffice.getCityOfficeName()) > 0){
            throw new IllegalArgumentException("City Office with name "+cityOffice.getCityOfficeName()+" already exists");
        }

        cityOffice.setCityOfficePassword(pswdEncoder.encode(cityOffice.getCityOfficePassword()));
        return cityOfficeRepo.save(cityOffice);
    }

    @Override
    public CityOffice updateCityOffice(CityOffice cityOffice){
        validate(cityOffice);
        Long officeId = cityOffice.getCityOfficeId();
        //Id doesn't exist
        if(!cityOfficeRepo.existsById(officeId))
            throw new EntityMissingException(CityOffice.class, officeId);

        //Email conflict
        if(cityOfficeRepo.existsByCityOfficeEmailAndCityOfficeIdNot(cityOffice.getCityOfficeEmail(), officeId)){
            throw new IllegalArgumentException("City Office with email "+cityOffice.getCityOfficeEmail() + " already exists");
        }

        //Name conflict
        if(cityOfficeRepo.existsByCityOfficeNameAndCityOfficeIdNot(cityOffice.getCityOfficeName(), officeId)){
            throw new IllegalArgumentException("City Office with name "+cityOffice.getCityOfficeName()+" already exists");
        }

        return cityOfficeRepo.save(cityOffice);
    }

    @Override
    public CityOffice deleteCityOffice(long cityOfficeId){
        CityOffice office = fetch(cityOfficeId);
        cityOfficeRepo.delete(office);
        return office;
    }

    private void validate(CityOffice office){
        Assert.notNull(office, "Office object must be given");

        //Email validation
        String email = office.getCityOfficeEmail();
        Assert.hasText(email, "Email can't be empty");
        Assert.isTrue(email.matches(EMAIL_REGEX), "Invalid email format");
        if(email.length()>100){
            Assert.isTrue(false,"Email can't be longer than 100 characters");
        }

        //Name validation
        String name = office.getCityOfficeName();
        Assert.hasText(name,"Username can't be empty");
        if(name.length()>100){
            Assert.isTrue(false, "Username can't be longer than 100 characters");
        }

        String password = office.getCityOfficePassword();
        Assert.hasText(password, "Password name can't be empty");
        if(password.length()<8){
            Assert.isTrue(false,"Password must have at least 8 characters");
        }else if(password.length()>100){
            Assert.isTrue(false, "Password can't be longer than 100 characters");
        }
    }
}
