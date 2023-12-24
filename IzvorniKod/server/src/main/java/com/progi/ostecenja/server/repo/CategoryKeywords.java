package com.progi.ostecenja.server.repo;

import jakarta.persistence.*;

@Entity(name="categoryKeywords")
public class CategoryKeywords {
    @Id @GeneratedValue
<<<<<<< HEAD
    private Long keywordID;

=======
    private long keywordID;
    @Column
>>>>>>> develop
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
