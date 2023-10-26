package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;

public interface StoreDao extends JpaRepository<Store, Long> {

}
