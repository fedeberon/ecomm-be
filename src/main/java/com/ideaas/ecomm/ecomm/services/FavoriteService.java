package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Favorite;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.dto.FavoriteDTO;
import com.ideaas.ecomm.ecomm.domain.dto.Username;
import com.ideaas.ecomm.ecomm.repository.FavoriteDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IFavoriteService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public FavoriteDTO save(Favorite favorite) {
        Long existantFavId = getFavoriteStatus(favorite.getUser(), favorite.getProduct());
        if (existantFavId != null)
            return null;

        return convertToFavoriteDTO(dao.save(favorite));
    }

    @Override
    public FavoriteDTO get(Long id) {
        Optional<Favorite> optFavorite = dao.findById(id);
        return convertToFavoriteDTO(optFavorite.get());
    }

    @Override
    public Long getFavoriteStatus (User user, Product product){
        Optional<Favorite> evaluated = dao.findByUserAndProduct(user, product);
        if (!evaluated.isPresent())
            return null;
        return evaluated.get().getId();
    }

    @Override
    public void delete(Long id){
        Favorite fav = dao.getById(id);
        dao.delete(fav);
    }

    @Override
    public Page<FavoriteDTO> findByUser(String username, Boolean asc, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size,
                asc ? Sort.by(Sort.Order.asc("id")) : Sort.by(Sort.Order.desc("id")));

        User user = userService.get(username).orElseThrow(() -> new RuntimeException("User not found"));
        Page<Favorite> favPage = dao.findAllByUser(user, pageable);

        return favPage.map(this::convertToFavoriteDTO);
    }

    @Override
    public List<FavoriteDTO> findAllByUser(String username) {
        User user = userService.get(username).orElseThrow(() -> new RuntimeException("User not found"));
        List<Favorite> favList = dao.findAllByUser(user);

        return favList.stream()
                .map(this::convertToFavoriteDTO)
                .collect(Collectors.toList());
    }

    private FavoriteDTO convertToFavoriteDTO(Favorite favorite) {
        return FavoriteDTO.builder()
                .id(favorite.getId())
                .user(new Username(favorite.getUser().getUsername()))
                .product(favorite.getProduct())
                .build();
    }
}
