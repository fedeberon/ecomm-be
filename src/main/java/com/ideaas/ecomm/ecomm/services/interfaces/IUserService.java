package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IUserService extends UserDetailsService {
    User save(User user);

    User update(User user);

    Optional<User> get(String username);

    List<User> findAll();

    User getCurrent();

}
