package com.progi.ostecenja.server.repo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "CityOffice")
public class CityOffice {
    @Id @GeneratedValue
  private long cityOfficeID;

  @Column(unique=true, nullable=false)
  private String cityOfficeName;

  @Column(unique=true, nullable=false)
  private String getCityOfficeEmail;

  @Column(unique=true, nullable=false)
  private String getCityOfficePassword;
  public long getCityOfficeID() {
        return cityOfficeID;
    }
    public String getCityOfficeName() {
        return cityOfficeName;
    }

    public String getGetCityOfficeEmail() {
        return getCityOfficeEmail;
    }

    public String getGetCityOfficePassword() {
        return getCityOfficePassword;
    }
    public CityOffice(){

    }
    public CityOffice(long cityOfficeID, String cityOfficeName, String getCityOfficeEmail, String getCityOfficePassword) {
        this.cityOfficeID = cityOfficeID;
        this.cityOfficeName = cityOfficeName;
        this.getCityOfficeEmail = getCityOfficeEmail;
        this.getCityOfficePassword = getCityOfficePassword;
    }

}
