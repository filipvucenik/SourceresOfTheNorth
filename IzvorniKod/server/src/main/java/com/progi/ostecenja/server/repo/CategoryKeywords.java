package com.progi.ostecenja.server.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity(name="categoryKeywords")
public class CategoryKeywords {
    @Id @GeneratedValue
    private Long keywordID;

    private String keyword;

    @JoinColumn
    private long categoryID;

    public CategoryKeywords(){}

    public CategoryKeywords(Long keywordID, String keyword, long categoryID) {
        this.keyword = keyword;
        this.categoryID = categoryID;
    }

    public long getKeywordID() {
        return keywordID;
    }

    public void setKeywordID(long keywordID) {
        this.keywordID = keywordID;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(long categoryID) {
        this.categoryID = categoryID;
    }
}
