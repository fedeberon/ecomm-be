package com.ideaas.ecomm.ecomm.payload;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.domain.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductPayload {

    private String name;

    private String description;

    private String code;

    private Double price;

    private Long stock;

    private Category category;

    private List<Size> sizes;

    private Brand brand;

    private Boolean promo = false;


}
