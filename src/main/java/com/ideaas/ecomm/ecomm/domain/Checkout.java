package com.ideaas.ecomm.ecomm.domain;

import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "CHECKOUT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CHE_ID")
    private Long id;

    @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductToCart> products;

    @Enumerated(EnumType.STRING)
    private CheckoutState checkoutState;



}
