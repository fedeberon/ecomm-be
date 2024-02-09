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

import java.util.*;
import java.util.Map.Entry;

@Service
public class UserService implements IUserService {

    private UserDao dao;
    private ScheduleDao scheduleDao;
    private IAuthenticationFacade authenticationFacade;
    private IRoleService roleService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    public Entry<Integer, UserDTO> save(UserDTO dto) {
        //Se evalua si el mismo usuario existe, de ser asi no se guarda
        if (getExistentUser(dto.getEmail()) != null) {
            return new AbstractMap.SimpleEntry<>(409, null);
        }

        //Se encripta la contraseña del nuevo usuario
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        return saveUser(dto);
    }

    @Override
    public Entry<Integer, UserDTO>  update(UserDTO dto) {
        //Se evalua si el mismo usuario existe, de no ser asi no hay actualizacion.
        User user = getExistentUser(dto.getEmail());
        if (user == null) {
            return new AbstractMap.SimpleEntry<>(409, null);
        }

        //Se conservara la misma contraseña (ya encriptada)
        dto.setPassword(user.getPassword());
        return saveUser(dto);
    }

    @Override
    public Entry<Integer, String>  updatePassword(final String username, final String password) {
        //Se verifica si el usuario ya existe: de no ser asi retorna error.
        User user = getExistentUser(username);
        if (user == null) {
            return new AbstractMap.SimpleEntry<>(412, "Error: user doesn't exist");
        }

        //Se encripta la contraseña del usuario
        user.setPassword(passwordEncoder.encode(password));
        dao.save(user);

        return new AbstractMap.SimpleEntry<>(202, "Password changed successfully");
    }

    private Entry<Integer, UserDTO> saveUser(UserDTO dto){
        //Se verifica si el rol ingresado es valido o no
        if (!Arrays.stream(CommerceRole.values()).anyMatch(val -> val.name().equals(dto.getRole().trim()))) {
            return new AbstractMap.SimpleEntry<>(412, null);
        }

        //Se guarda el usuario y se le asigna un rol en la base de datos.
        User saved = dao.save(generateUser(dto));
        roleService.assign(saved, dto.getRole());

        //A la confirmacion de los datos se le borra la confirmacion encriptada
        dto.setPassword(null);

        return new AbstractMap.SimpleEntry<>(202, dto);
    }

    private User getExistentUser(String username){
        return dao.findByUsername(username);
    }

    private User generateUser(final UserDTO dto){
        User user = User.builder()
                .username(dto.getEmail())
                .password(dto.getPassword())
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
