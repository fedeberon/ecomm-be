package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.AFIP.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressDao extends JpaRepository<Address, Long> {
}
