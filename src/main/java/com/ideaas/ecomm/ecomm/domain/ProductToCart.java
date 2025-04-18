package com.ideaas.ecomm.ecomm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "PRODUCT_TO_CART")

public class ProductToCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PTC_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PTC_PRO_ID", nullable = false)
    private Product product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "CHE_ID")
    private Checkout checkout;

    @Column(name = "PTC_QUANTITY")
    private Integer quantity;

    @Getter(AccessLevel.NONE)
    @Column(name = "PTC_PRICE", precision=10, scale=2)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "PTC_SIZE_ID")
    private Size size;


    public Double getPrice() {
        try {
            return price * quantity;
        } catch (Exception ex) {
            return 0.0;
        }
    }

}
