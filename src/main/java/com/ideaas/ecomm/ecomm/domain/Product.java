package com.ideaas.ecomm.ecomm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRO_ID")
    private Long id;

    @Column(name = "PRO_NAME")
    private String name;

    @Column(name = "PRO_DESCRIPTION")
    private String description;

    @Column(name = "PRO_CODE")
    private String code;

    @Column(name = "PRO_PRICE", precision=10, scale=2)
    private Double price;

    @Column(name = "PRO_STOCK")
    private Long stock;

    @Transient
    private List<Image> images;

    @OneToOne
    @JoinColumn(name = "PROD_CAT_ID")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<SizesByProduct> sizesByProducts;

    @OneToOne
    @JoinColumn(name = "PROD_BRAND_ID")
    private Brand brand;

    @Column(name = "PRO_POINT")    
    private Long points = 0L;

    @Column(name = "PRO_PROMO")    
    private Boolean promo = false;

    @Transient
    private List<Size> sizes;

    public void setSizesByProducts() {
        List<SizesByProduct> sizesByProducts = new ArrayList<>();
        sizes.forEach(size -> {
            SizesByProduct sizesByProduct = new SizesByProduct();
            sizesByProduct.setSize(size);
            sizesByProducts.add(sizesByProduct);
        });
        sizesByProducts.forEach(size -> {
            size.setProduct(this);
        });

        this.sizesByProducts = sizesByProducts;
    }

}
