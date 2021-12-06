package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.repository.UserDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IAuthenticationFacade;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        user.setUsername(user.getCardId());

        return dao.save(user);
    }

    @Override
    public User get(String username) {
        return dao.findById(username).get();
    }

    @Override
    public List<User> findAll() {
        return dao.findAll();
    }


    @Override
    public User getCurrent(){
        String username = authenticationFacade.getAuthentication().getName();
        return get(username);
    }

}
