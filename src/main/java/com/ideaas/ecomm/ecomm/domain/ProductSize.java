package com.ideaas.ecomm.ecomm.domain;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "PROD_SIZE")
public class ProductSize {

    @OneToMany(mappedBy = "Product")
    @Column(name = "PROD_ID")
    private Long productID;

    @OneToMany(mappedBy = "Size")
    @Column(name = "SIZE_Id")
    private Long sizeID;
    
}
