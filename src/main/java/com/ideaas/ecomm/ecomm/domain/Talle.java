package com.ideaas.ecomm.ecomm.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SIZE")
public class Talle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SIZE_ID")
    private Long id;

    @Column(name = "SIZE_NAME")
    private String name;
}
