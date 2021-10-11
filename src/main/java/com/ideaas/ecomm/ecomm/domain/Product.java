package com.ideaas.ecomm.ecomm.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCTS")
@Getter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRO_ID")
    private Long id;

    @Column(name = "PRO_NAME")
    private String name;

    @Column(name = "PRO_PRICE")
    private Long price;



}
