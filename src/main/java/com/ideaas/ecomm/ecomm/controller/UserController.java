package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Store;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.Wallet;
import com.ideaas.ecomm.ecomm.domain.dto.UserDTO;
import com.ideaas.ecomm.ecomm.domain.dto.WalletDTO;
import com.ideaas.ecomm.ecomm.services.UserService;
import com.ideaas.ecomm.ecomm.services.interfaces.IStoreService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import com.ideaas.ecomm.ecomm.services.interfaces.IWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController {

    private IUserService userService;
    private IWalletService walletService;
    private IStoreService storeService;


    @Autowired
    public UserController(final IUserService userService,
                          final IWalletService walletService,
                          final IStoreService storeService) {
        this.userService = userService;
        this.walletService = walletService;
        this.storeService = storeService;
    }

    /*Codigos retornados:
     * 202: OK
     * 412: Precondition Failed - cuando el valor del rol no es aceptable.
     * 409: Conflict - cuando el usuario ya existe dentro de la base (usar update en lugar de save)
     */

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody final UserDTO dto) {
        final Entry<Integer, UserDTO> result = userService.save(dto);
        return ResponseEntity.status(result.getKey()).body(result.getValue());
    }

    @PutMapping
    public ResponseEntity<UserDTO> update(@RequestBody final UserDTO dto) {
        Entry<Integer, UserDTO> result = userService.update(dto);
        return ResponseEntity.status(result.getKey()).body(result.getValue());
    }

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String password = requestBody.get("password");
        Entry<Integer, String> result = userService.updatePassword(username, password);
        return ResponseEntity.status(result.getKey()).body(result.getValue());
    }

    @GetMapping
    private ResponseEntity<List<UserDTO>> findAll(){
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("{username}")
    public ResponseEntity<UserDTO> findByUsername(@PathVariable final String username){
        final UserDTO user = userService.getDTO(username).get();
        return ResponseEntity.ok(user);
    }


    @GetMapping("/wallet/{username}")
    public ResponseEntity<List<WalletDTO>> getWalletByUser(@PathVariable final String username) {
        final User user = userService.get(username).get();
        final List<WalletDTO> walletOfUser = walletService.findAllByUser(user);

        return ResponseEntity.ok(walletOfUser);
    }

    @GetMapping("/wallet/points/{username}")
    public ResponseEntity<Long> getPointsWalletByUser(@PathVariable final String username) {
        final User user = userService.get(username).get();
        Long points = walletService.getActivePointsWalletByUser(user);

        return ResponseEntity.ok(points);
    }

    /*
    @PostMapping("twins")
    public ResponseEntity<User> updateTwins(@RequestBody final User user) {
        User userToUpdate = userService.get(user.getCardId()).get();
        userToUpdate.setTwins(user.getTwins());
        //userService.update(userToUpdate, "");

        return ResponseEntity.status(202).body(null);
    }
    */

    @GetMapping("/{username}/stores")
    public ResponseEntity<Set<Store>> getUserStores(@PathVariable String username) {
        Set<Store> userStores = storeService.getStoresByUser(username);
        return ResponseEntity.ok(userStores);
    }
}
