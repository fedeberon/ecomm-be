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

    @Column(name = "STO_EMAIL")
    private String email;

    @Column(name = "STO_TEL")
    private String telephone;

    @Column(name = "STO_ADDRESS")
    private String address;

    @OneToOne
    @JoinColumn(name = "STO_SCH_ID")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn(name = "STO_USU_USERNAME")
    private User owner;

    @Transient
    private Image logo;
}
