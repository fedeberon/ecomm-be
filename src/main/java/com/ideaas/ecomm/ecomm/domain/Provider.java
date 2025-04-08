package com.ideaas.ecomm.ecomm.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "PROVIDERS")
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRO_ID")
    private Long id;

    @Column(name = "PRO_NAME")
    private String name;

    @Column(name = "PRO_ADDRESS")
    private String address;

    @Column(name = "PRO_CUIT")
    private String cuit;
}
