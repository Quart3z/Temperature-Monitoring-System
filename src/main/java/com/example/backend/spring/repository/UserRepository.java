package com.example.backend.spring.repository;

import com.example.backend.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    // Valid the user status
    @Query(value = "SELECT * FROM User JOIN Token ON Token.user_id = User.id WHERE Token.token = :token", nativeQuery = true)
    User findUserByToken(@Param("token") String token);

}
