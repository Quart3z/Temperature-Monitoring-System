package com.example.backend.spring.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private final String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private final User user;

    public Token(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
    }

    public String getToken() {
        return token;
    }

}
