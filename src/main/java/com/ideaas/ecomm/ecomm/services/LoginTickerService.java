package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.repository.LoginTicketDao;
import com.ideaas.ecomm.ecomm.services.interfaces.ILoginTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LoginTickerService implements ILoginTicketService {

    private LoginTicketDao dao;

    @Autowired
    public LoginTickerService(LoginTicketDao dao) {
        this.dao = dao;
    }


    @Override
    public LoginTicketResponse save(final LoginTicketResponse loginTicket){
        LoginTicketResponse loginTicketResponse = dao.save(loginTicket);

        return loginTicketResponse;
    }

    @Override
    public Optional<LoginTicketResponse> getActive(final String service){
        return dao.findAllByExpirationTimeBeforeAndServiceEquals(LocalDateTime.now(), service);
    }

}
