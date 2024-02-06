package com.ideaas.ecomm.ecomm.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FAVORITES")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "FAV_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USU_USERNAME", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "PRO_ID", nullable = false)
    private Product product;
}
