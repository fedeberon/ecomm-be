package com.ideaas.ecomm.ecomm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "SIZES_BY_PRODUCT")
public class SizesByProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SBP_ID")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "SBP_PRO_ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "SBP_SIZE_ID", nullable = false)
    private Size size;



}
