package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.dto.UserDTO;
import com.ideaas.ecomm.ecomm.enums.CommerceRole;
import com.ideaas.ecomm.ecomm.repository.ScheduleDao;
import com.ideaas.ecomm.ecomm.repository.UserDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IAuthenticationFacade;
import com.ideaas.ecomm.ecomm.services.interfaces.IRoleService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private UserDao dao;
    private ScheduleDao scheduleDao;
    private IAuthenticationFacade authenticationFacade;
    private IRoleService roleService;

    @Autowired
    public UserService(final UserDao dao,
                       final IAuthenticationFacade authenticationFacade,
                       final IRoleService roleService) {
        this.dao = dao;
        this.authenticationFacade = authenticationFacade;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = dao.getById(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());

    }



    private User setPassword(User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(user.getPassword());;
        user.setPassword(password);
        return user;
    }

    @Override
    public Optional<User> save(final UserDTO dto) {
        User existingUser = dao.findByUsername(dto.getEmail());
        if (existingUser != null) {
            return null;
        }else if (!Arrays.stream(CommerceRole.values()).anyMatch(val -> val.name().equals(dto.getRole().trim()))) {
            return Optional.empty();
        }

        User newUser = generateUser(dto);

        User saved = dao.save(newUser);
        roleService.assign(newUser, dto.getRole());
        return Optional.of(saved);
    }

    @Override
    public User update(final User user, final String role) {
        setPassword(user);
        User saved = dao.save(user);
        roleService.assign(user, role);
        return saved;
    }

    private String encryptPassword(String passwrd){
        BCryptPasswordEncoder passwordEncoder =new BCryptPasswordEncoder();
        return passwordEncoder.encode(passwrd);
    }

    private User generateUser(final UserDTO dto){
        User user = User.builder()
                .username(dto.getEmail())
                .password(encryptPassword(dto.getPassword()))
                .name(dto.getName())
                .lastName(dto.getLastName())
                .cuit(dto.getCuit())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .city(dto.getCity())
                .direction(dto.getDirection())
                .postal(dto.getPostal())
                .build();
        return user;
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
