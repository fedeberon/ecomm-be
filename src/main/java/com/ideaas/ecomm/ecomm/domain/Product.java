package com.ideaas.ecomm.ecomm.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PRODUCTS")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRO_ID")
    private Long id;

    @Column(name = "PRO_NAME")
    private String name;

    @Column(name = "PRO_DESCRIPTION")
    private String description;

    @Column(name = "PRO_CODE")
    private String code;

    @Column(name = "PRO_PRICE", precision=10, scale=2)
    private Double price;

    @Column(name = "PRO_STOCK")
    private Long stock;

    @Transient
    private List<Image> images;

    @OneToOne
    @JoinColumn(name = "PROD_CAT_ID")
    private Category category;

    @OneToOne
    @JoinColumn(name = "PROD_BRAND_ID")
    private Brand brand;

    @Column(name = "PRO_POINTS")
    private Long points;

}
