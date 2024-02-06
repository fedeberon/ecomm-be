package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Favorite;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.exception.NotFoundException;
import com.ideaas.ecomm.ecomm.services.interfaces.IFavoriteService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/favorite")
@CrossOrigin
public class FavoriteController {
    private IFavoriteService favoriteService;
    private IProductService productService;
    private IUserService userService;

    @Autowired
    public FavoriteController(final IFavoriteService favoriteService,
                              final IProductService productService,
                              final IUserService userService) {
        this.favoriteService = favoriteService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Page<Favorite>> findByUser(@PathVariable String username,
                                     @RequestParam(defaultValue = "true") final Boolean asc,
                                     @RequestParam(defaultValue = "0") final Integer page,
                                     @RequestParam(defaultValue = "10") final Integer size) {
        try{
            Page<Favorite> favPage = favoriteService.findByUser(username, asc, page, size);
            return ResponseEntity.ok(favPage);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Favorite> get(@PathVariable Long id) {
        try {
            Favorite favorite = favoriteService.get(id);
            return ResponseEntity.ok().body(favorite);
        }catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public  ResponseEntity<Favorite> save(@RequestBody Map<String, String> request) {
        //Se obtienen los datos
        Long productId = Long.parseLong(request.get("productId"));
        String username = request.get("username");
        //Se obtienen el usuario y el producto
        User user = userService.get(username).get();
        Product product = productService.get(productId);
        //Se asocian en un favorito nuevo
        Favorite favorite = Favorite.builder().user(user).product(product).build();
        //Se envia al servicio para guardarlo.
        Favorite favSaved = favoriteService.save(favorite);
        return ResponseEntity.ok(favSaved);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> delete(@PathVariable Long id) {
        final Favorite favToDelete = favoriteService.get(id);
        favoriteService.delete(favToDelete);

        return ResponseEntity.accepted().body("Favourite with id #" + favToDelete.getId() + " deleted succesfully.");
    }
}
