package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

public interface IUserService extends UserDetailsService {
    Entry<Integer,UserDTO> save(UserDTO dto);

    Entry<Integer, UserDTO>  update(UserDTO user);
    Entry<Integer, String>  updatePassword(final String username, final String password);

    Optional<UserDTO> getDTO(String username);//Para devolver solicitudes
    Optional<User> get(String username);//Para trabajo con wallets

    List<UserDTO> findAll();

    UserDTO getCurrent();

}
