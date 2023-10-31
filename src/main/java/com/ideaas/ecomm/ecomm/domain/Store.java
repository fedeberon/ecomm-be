package com.ideaas.ecomm.ecomm.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "STORES")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STO_ID")
    private Long id;

    @Column(name = "STO_NAME")
    private String name;

    @Column(name = "STO_DESCR")
    private String description;

    @OneToOne
    @JoinColumn(name = "STO_USU_USERNAME")
    private User owner;

    @Transient
    private Image logo;

}
