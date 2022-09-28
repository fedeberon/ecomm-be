package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.Wallet;
import com.ideaas.ecomm.ecomm.services.UserService;
import com.ideaas.ecomm.ecomm.services.interfaces.IWalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController {

    private UserService userService;
    private IWalletService walletService;


    @Autowired
    public UserController(final UserService userService,
                            final IWalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody final User user) {
        final User userSaved = userService.save(user);

        return ResponseEntity.status(202).body(userSaved);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody final User user) {
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


    @GetMapping("/wallet/{username}")
    public ResponseEntity<List<Wallet>> getWalletByUser(@PathVariable final String username) {
        final User user = userService.get(username);
        final List<Wallet> walletOfUser = walletService.findAllByUser(user);     

        return ResponseEntity.ok(walletOfUser);
    }

    @GetMapping("/wallet/points/{username}")
    public ResponseEntity<Long> getPointsWalletByUser(@PathVariable final String username) {
        final User user = userService.get(username);
        Long points = walletService.getPointsWalletByUser(user);

        return ResponseEntity.ok(points);
    }

    @PostMapping("twins")
    public ResponseEntity<User> updateTwins(@RequestBody final User user) {
        User userToUpdate = userService.get(user.getCardId());
        userToUpdate.setTwins(user.getTwins());
        userService.update(userToUpdate);

        return ResponseEntity.status(202).body(userToUpdate);
    }

}
