package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Favorite;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.dto.FavoriteDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IFavoriteService {
    FavoriteDTO save (Favorite favorite);
    FavoriteDTO get (Long id);

    Long getFavoriteStatus (User user, Product product);
    void delete(Long id);
    Page<FavoriteDTO> findByUser (String username, Boolean asc, Integer page, Integer size);

    List<FavoriteDTO> findAllByUser (String username);
}
