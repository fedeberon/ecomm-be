package com.ideaas.ecomm.ecomm.repository;


import com.ideaas.ecomm.ecomm.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
