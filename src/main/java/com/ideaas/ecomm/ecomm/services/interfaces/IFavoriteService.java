package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Favorite;
import org.springframework.data.domain.Page;

public interface IFavoriteService {
    Favorite save (Favorite favorite);
    Favorite get (Long id);
    void delete(Favorite favorite);
    Page<Favorite> findByUser (String username, Boolean asc, Integer page, Integer size);
}
