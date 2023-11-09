package com.progi.ostecenja.server.repo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity(name="USERS")
public class Users {
    @Id
    @GeneratedValue
    private Long userId;
    @Column(unique = true, nullable = false)
    @NotBlank
    @Size(max = 100)
    @Email
    private String email;
    @Column(unique = true)
    @NotBlank
    @Size(max=100)
    private String userName;
    @NotBlank
    @Size(min=8, max=100)
    private String password;
    @NotBlank
    @Size(max=50)
    private String firstName;
    @NotBlank
    @Size(max=50)
    private String lastName;

    public Users(){}
    public Users(Long userId, String email, String firstName, String lastName, String password, String userName) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userName = userName;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
