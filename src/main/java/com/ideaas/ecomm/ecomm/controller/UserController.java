package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody final User user) {
        final User userSaved = userService.save(user);

        return ResponseEntity.status(202).body(userSaved);
    }

    @GetMapping
    private ResponseEntity<List<User>> findAll(){
        List<User> users = userService.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("{username}")
    public ResponseEntity<User> findByUsername(@PathVariable final String username){
        final User user = userService.get(username);

        return ResponseEntity.ok(user);
    }

}
