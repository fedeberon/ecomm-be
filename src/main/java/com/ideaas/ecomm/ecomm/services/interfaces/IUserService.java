package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Role;
import com.ideaas.ecomm.ecomm.domain.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();
    User get(String username);
    User save(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
}
