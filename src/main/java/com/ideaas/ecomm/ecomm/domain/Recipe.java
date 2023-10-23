package com.ideaas.ecomm.ecomm.domain;

import java.util.List;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "RECIPE")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRO_ID")
    private Long id;

    @Column(name = "contact")
    private String contact;

    @Column(name = "cuil")
    private String cuil;

    @Transient
    private List<Image> images;
    
}
