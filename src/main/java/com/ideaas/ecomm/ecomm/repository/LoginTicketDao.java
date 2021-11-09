package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LoginTicketDao extends JpaRepository<LoginTicketResponse, Long> {

    Optional<LoginTicketResponse> findByExpirationTimeAfter(LocalDateTime now);

    default Optional<LoginTicketResponse> getActive(LocalDateTime localDate) {
        return findByExpirationTimeAfter(localDate);
    }

}
