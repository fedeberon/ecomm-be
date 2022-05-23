package com.ideaas.ecomm.ecomm.payload;

import com.ideaas.ecomm.ecomm.domain.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductResponse {

    private List<Product> products;

    private int totalPages;

}
