package com.example.backend.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity
@Table
public class User {

    /**
     * ====================
     * Attributes & columns
     * ====================
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    // status: 0 - unverified,  1 - verified
    @Column(nullable = false)
    private int status;

    /**
     * =================
     * Class constructor
     * =================
     */
    public  User(){}

    public User(String password, String username, String email, int status){
        this.password = new BCryptPasswordEncoder().encode(password);
        this.username = username;
        this.email = email;
        this.status = status;
    }

    /**
     * =======================
     * Accessors and mutators
     * =======================
     */
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}

