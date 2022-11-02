package com.ideaas.ecomm.ecomm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.Wallet;

public interface WalletDao extends JpaRepository<Wallet, Long>{

   List<Wallet> findByUserAndAndDateAfter(User user, LocalDateTime date);

   List<Wallet> findAllByUser(User user);

}
