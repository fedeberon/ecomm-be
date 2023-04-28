package com.ideaas.ecomm.ecomm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ITEM_STOCK")
public class ItemStock {

    @Id
    @Column(name = "IT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "IT_QUANTITY")
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "IT_PRO_ID", nullable = false)
    private Product product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "IT_ST_ID")
    private Stock stock;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "ST_SIZE_ID", nullable = false)
    private Size size;

}
