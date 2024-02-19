package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Favorite;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IFavoriteService {
    Favorite save (Favorite favorite);
    Favorite get (Long id);

    Long getFavoriteStatus (User user, Product product);
    void delete(Favorite favorite);
    Page<Favorite> findByUser (String username, Boolean asc, Integer page, Integer size);

    List<Favorite> findAllByUser (String username);
}
