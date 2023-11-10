package com.progi.ostecenja.server.repo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity(name = "CityOffice")
public class CityOffice {
  @Id @GeneratedValue
  private Long cityOfficeId;

  @Column(unique=true, nullable=false)
  private String cityOfficeName;

  @Column(unique=true, nullable=false)
  private String cityOfficeEmail;
  @Column(nullable=false)
  private String cityOfficePassword;

  public Long getCityOfficeId() {
        return cityOfficeId;
    }

  public String getCityOfficeName() {
        return cityOfficeName;
    }

  public String getCityOfficeEmail() {
        return cityOfficeEmail;
    }

  public String getCityOfficePassword() {
        return cityOfficePassword;
    }

  public CityOffice(){

  }
  public CityOffice(long cityOfficeId, String cityOfficeName, String cityOfficeEmail, String cityOfficePassword) {
        this.cityOfficeId = cityOfficeId;
        this.cityOfficeName = cityOfficeName;
        this.cityOfficeEmail = cityOfficeEmail;
        this.cityOfficePassword = cityOfficePassword;
  }

    @Override
    public String toString() {
        return "CityOffice{" +
                "cityOfficeId=" + cityOfficeId +
                ", cityOfficeName='" + cityOfficeName + '\'' +
                ", cityOfficeEmail='" + cityOfficeEmail + '\'' +
                ", cityOfficePassword='" + cityOfficePassword + '\'' +
                '}';
    }

}
