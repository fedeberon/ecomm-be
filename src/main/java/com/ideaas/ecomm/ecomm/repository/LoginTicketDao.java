package com.ideaas.ecomm.ecomm.repository;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LoginTicketDao extends JpaRepository<LoginTicketResponse, Long> {

    Optional<LoginTicketResponse> findAllByExpirationTimeBeforeAndServiceEquals(final LocalDateTime now,
                                                                                final String service);

}
