package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Role;
import com.ideaas.ecomm.ecomm.domain.User;

public interface IRoleService {
    void assign(User user, String role);

    Role get(User user);
}
