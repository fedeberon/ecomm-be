package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Role;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {
    private final IUserService userService;

    @GetMapping("/findAll")
    public ResponseEntity<List<User>>findAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @PostMapping("/save")
    public ResponseEntity<User>saveUser(@RequestBody User user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/eComm/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.save(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/eComm/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }
}
