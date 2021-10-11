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

    @Column(name = "PRO_PRICE")
    private Long price;

    @Transient
    private List<Image> images;

}
