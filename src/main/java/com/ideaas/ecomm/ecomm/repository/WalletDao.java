package com.ideaas.ecomm.ecomm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.Wallet;

public interface WalletDao extends JpaRepository<Wallet, Long>{

   List<Wallet>  findByUser(User user); 


}
