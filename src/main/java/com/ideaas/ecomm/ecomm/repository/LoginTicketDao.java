package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginTicketDao extends JpaRepository<LoginTicketResponse, Long> {
}
