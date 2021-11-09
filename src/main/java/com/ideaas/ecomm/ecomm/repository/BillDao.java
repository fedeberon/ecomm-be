package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillDao extends JpaRepository<Bill, Long> {
}
