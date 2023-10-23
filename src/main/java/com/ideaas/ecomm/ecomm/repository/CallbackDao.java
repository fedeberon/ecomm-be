package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.Callback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallbackDao extends JpaRepository<Callback, Long> {
    Callback findByCollectionId(String callCollectionId);
}
