package com.ideaas.ecomm.ecomm.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "PRODUCTS")
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

    @ManyToMany
    @JoinTable(name = "PROD_SIZE",
                joinColumns = @JoinColumn(name="PRO_ID"),
                inverseJoinColumns =@JoinColumn(name = "SIZE_ID"))
    @Column
    private List<Talle> talle;


    @OneToOne
    @JoinColumn(name = "PROD_BRAND_ID")
    private Brand brand;

    @Column(name = "PRO_POINT")    
    private Long points = 0L;

    @Column(name = "PRO_PROMO")    
    private Boolean promo = false;

}
