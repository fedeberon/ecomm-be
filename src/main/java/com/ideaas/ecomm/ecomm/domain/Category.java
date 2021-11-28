package com.ideaas.ecomm.ecomm.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "CATEGORIES")
public class Category {

    @Id
    @Column(name = "CAT_ID")
    private Long id;

    @Column(name = "CAT_NAME")
    private String name;
}
