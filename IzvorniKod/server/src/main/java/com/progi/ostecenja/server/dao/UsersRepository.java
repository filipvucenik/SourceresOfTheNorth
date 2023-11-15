package com.progi.ostecenja.server.dao;

import com.progi.ostecenja.server.repo.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    int countByEmail(String email);
    Optional<Users> findByUserId(long userId);
    Optional<Users> findByEmail(String email);

    // Note: exists- query is the best if we just want to predict conflicts
    boolean existsByEmailAndUserIdNot(String email, Long id);
}
