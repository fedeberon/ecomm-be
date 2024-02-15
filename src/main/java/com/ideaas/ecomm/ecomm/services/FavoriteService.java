package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Favorite;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.repository.FavoriteDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IFavoriteService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavoriteService implements IFavoriteService {
    private FavoriteDao dao;
    private IUserService userService;

    @Autowired
    public FavoriteService(final FavoriteDao dao, final IUserService userService){
        this.dao = dao;
        this.userService = userService;
    }

    @Override
    public Favorite save(Favorite favorite) {
        Long existantFavId = getFavoriteStatus(favorite.getUser(), favorite.getProduct());
        if (existantFavId != null)
            return null;

        return dao.save(favorite);
    }

    @Override
    public Favorite get(Long id) {
        Optional<Favorite> optFavorite = dao.findById(id);
        return optFavorite.get();
    }

    @Override
    public Long getFavoriteStatus (User user, Product product){
        Optional<Favorite> evaluated = dao.findByUserAndProduct(user, product);
        if (!evaluated.isPresent())
            return null;
        return evaluated.get().getId();
    }

    @Override
    public void delete(Favorite favorite){ dao.delete(favorite);}

    @Override
    public Page<Favorite> findByUser(String username, Boolean asc, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size,
                asc ?
                        Sort.by(Sort.Order.asc("id")) :
                        Sort.by(Sort.Order.desc("id"))
        );
        User user = userService.get(username).get();
        return dao.findAllByUser(user, pageable);
    }
}
