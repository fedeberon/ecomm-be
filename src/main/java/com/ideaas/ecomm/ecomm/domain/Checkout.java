package com.ideaas.ecomm.ecomm.domain;

import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name = "USU_CHE_ID")
    private String username;

    public Double getTotalAmount() {
        return products.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
    }

}
