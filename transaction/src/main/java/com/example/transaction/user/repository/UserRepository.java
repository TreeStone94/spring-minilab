package com.example.transaction.user.repository;

import com.example.transaction.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	// 사용자명으로 조회
    Optional<User> findByName(String name);

    // 이메일로 조회
    Optional<User> findByEmail(String email);
}
