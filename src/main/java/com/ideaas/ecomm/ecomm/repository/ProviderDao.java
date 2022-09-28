package com.ideaas.ecomm.ecomm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ideaas.ecomm.ecomm.domain.Provider;

@Repository
public interface ProviderDao extends JpaRepository<Provider,Long>{

    Optional<Provider> findById(Long id);

   
}
