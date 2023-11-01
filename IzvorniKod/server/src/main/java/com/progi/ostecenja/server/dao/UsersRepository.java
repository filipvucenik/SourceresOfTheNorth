package com.progi.ostecenja.server.dao;

import com.progi.ostecenja.server.repo.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

}
