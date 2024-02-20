package com.ideaas.ecomm.ecomm.domain.dto;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.User;
import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
public class FavoriteDTO {

    private Long id;

    private Username user;

    private Product product;
}
