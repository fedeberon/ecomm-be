package com.ideaas.ecomm.ecomm.domain;

import com.ideaas.ecomm.ecomm.enums.StockType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "STOCK")
public class Stock {

    @Id
    @Column(name = "ST_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ST_TYPE")
    @Enumerated(EnumType.STRING)
    private StockType stockType;

    @Column(name = "ST_REF")
    private String referenceId;

    @Column(name = "ST_QUANTITY")
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "ST_PRO_ID", nullable = false)
    private Product product;

    @Column(name = "ST_DATE")
    private LocalDateTime date = LocalDateTime.now();

}
