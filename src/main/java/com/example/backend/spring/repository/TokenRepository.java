package com.example.backend.spring.repository;

import com.example.backend.spring.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
    Token findByToken(String token);
}
