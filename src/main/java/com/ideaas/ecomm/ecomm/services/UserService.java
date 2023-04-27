package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.repository.UserDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IAuthenticationFacade;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private UserDao dao;
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public UserService(final UserDao dao,
                       final IAuthenticationFacade authenticationFacade) {
        this.dao = dao;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = dao.getById(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());

    }

    @Override
    public User save(final User user) {
        BCryptPasswordEncoder passwordEncoder =new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(user.getPassword());
        user.setUsername(user.getEmail());
        user.setPassword(password);

        return dao.save(user);
    }

    @Override
    public User update(final User user) { 
        return dao.save(user);
    }

    @Override
    public Optional<User> get(String username) {
        return dao.findById(username);
    }

    @Override
    public List<User> findAll() {
        return dao.findAll();
    }


    @Override
    public User getCurrent(){
        String username = authenticationFacade.getAuthentication().getName();
        Optional<User> user = get(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

}
