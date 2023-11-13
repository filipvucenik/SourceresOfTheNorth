package com.progi.ostecenja.server.repo;

import jakarta.persistence.*;

@Entity(name = "Category")
public class Category {
    @Id @GeneratedValue
    private Long categoryID;
    @Column(unique = true, nullable = false)
    private String categoryName;

    @JoinColumn(name = "cityOfficeID")
    private Long cityOfficeID;

    public Long getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Long getCityOfficeID() {
        return cityOfficeID;
    }

    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCityOfficeID(Long cityOfficeID) {
        this.cityOfficeID = cityOfficeID;
    }


    public Category() {
    }
    public Category(Long catgoryID, String categoryName, Long cityOfficeID) {
        this.categoryID = catgoryID;
        this.categoryName = categoryName;
        this.cityOfficeID = cityOfficeID;
    }
}
