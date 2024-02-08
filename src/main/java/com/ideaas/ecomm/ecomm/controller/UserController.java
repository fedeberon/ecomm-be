package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Store;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.dto.UserDTO;
import com.ideaas.ecomm.ecomm.domain.Wallet;
import com.ideaas.ecomm.ecomm.services.UserService;
import com.ideaas.ecomm.ecomm.services.interfaces.IStoreService;
import com.ideaas.ecomm.ecomm.services.interfaces.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController {

    private UserService userService;
    private IWalletService walletService;
    private IStoreService storeService;


    @Autowired
    public UserController(final UserService userService,
                          final IWalletService walletService,
                          final IStoreService storeService) {
        this.userService = userService;
        this.walletService = walletService;
        this.storeService = storeService;
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody final UserDTO dto) {
        Optional<User> optionalUser = userService.save(dto);

        /*Codigos retornados:
        * 202: OK
        * 412: Precondition Failed - cuando el valor del rol no es aceptable.
        * 409: Conflict - cuando el usuario ya existe dentro de la base (usar update en lugar de save)
        */
        return optionalUser
                .map(savedUser -> ResponseEntity.status(202).body(savedUser))
                .orElseGet(() -> ResponseEntity.status(optionalUser.isPresent() ? 412 : 409).build());

    }

    @PutMapping("/{role}")
    public ResponseEntity<User> update(@PathVariable String role, @RequestBody final User user) {
        final User userSaved = userService.update(user, role);
        return ResponseEntity.status(202).body(userSaved);
    }


    @GetMapping
    private ResponseEntity<List<User>> findAll(){
        List<User> users = userService.findAll();

        return ResponseEntity.ok(users);
    }

    @GetMapping("{username}")
    public ResponseEntity<User> findByUsername(@PathVariable final String username){
        final User user = userService.get(username).get();

        return ResponseEntity.ok(user);
    }


    @GetMapping("/wallet/{username}")
    public ResponseEntity<List<Wallet>> getWalletByUser(@PathVariable final String username) {
        final User user = userService.get(username).get();
        final List<Wallet> walletOfUser = walletService.findAllByUser(user);     

        return ResponseEntity.ok(walletOfUser);
    }

    @GetMapping("/wallet/points/{username}")
    public ResponseEntity<Long> getPointsWalletByUser(@PathVariable final String username) {
        final User user = userService.get(username).get();
        Long points = walletService.getActivePointsWalletByUser(user);

        return ResponseEntity.ok(points);
    }

    @PostMapping("twins")
    public ResponseEntity<User> updateTwins(@RequestBody final User user) {
        User userToUpdate = userService.get(user.getCardId()).get();
        userToUpdate.setTwins(user.getTwins());
        userService.update(userToUpdate, "");

        return ResponseEntity.status(202).body(userToUpdate);
    }

    @GetMapping("/{username}/stores")
    public ResponseEntity<Set<Store>> getUserStores(@PathVariable String username) {
        Set<Store> userStores = storeService.getStoresByUser(username);
        return ResponseEntity.ok(userStores);
    }
}
