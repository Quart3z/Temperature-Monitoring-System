package com.example.backend.spring.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
public class Token {

    /**
     * ====================
     * Attributes & columns
     * ====================
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    /**
     * One-to-one relationship with User table
     * foreign key - user_id
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    /**
     * =================
     * Class constructor
     * =================
     */
    public Token() {
    }

    public Token(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
    }

    /**
     * =======================
     * Accessors and mutators
     * =======================
     */
    public String getToken() {
        return token;
    }

}
