package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Role;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.repository.RoleDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {
    private RoleDao dao;

    @Autowired
    public RoleService(RoleDao dao){this.dao = dao;}
    @Override
    public void assign(User user, String role){
        //Elimina el rol anterior (si existe)...
        Role existent = dao.findByUser(user);
        if (existent != null){
            dao.delete(existent);
        }
        Role assigned = Role.builder().user(user).role(role).build();
        dao.save(assigned);
    }

    @Override
    public Role get(User user){
        return dao.findByUser(user);
    }
}
