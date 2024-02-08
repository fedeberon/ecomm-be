package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IUserService extends UserDetailsService {
    Optional<User> save(UserDTO dto);

    User update(User user, String role);

    Optional<User> get(String username);

    List<User> findAll();

    User getCurrent();

}
