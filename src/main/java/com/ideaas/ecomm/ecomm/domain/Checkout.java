package com.ideaas.ecomm.ecomm.domain;

import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "CHE_DATE_TIME")
    private LocalDateTime dateTime;

    @OneToMany(mappedBy = "checkout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductToCart> products;

    @Enumerated(EnumType.STRING)
    private CheckoutState checkoutState;

    @Column(name = "USU_CHE_ID")
    private String username;

    public String getStatus() {
        return checkoutState.getValue();
    }

    public Double getTotalAmount() {
        return products.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
    }

}
