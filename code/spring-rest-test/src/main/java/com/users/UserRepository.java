package com.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.users.domain.User;

@Component
public interface UserRepository extends JpaRepository<User, Long> {
}
