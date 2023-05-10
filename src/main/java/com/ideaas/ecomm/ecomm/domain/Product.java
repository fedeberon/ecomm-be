package com.ideaas.ecomm.ecomm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name = "PRO_DESCRIPTION", length = 9999)
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

    @OneToOne
    @JoinColumn(name = "PROD_BRAND_ID")
    private Brand brand;

    @Column(name = "PRO_POINT")    
    private Long points = 0L;

    @Column(name = "PRO_AMOUNT_SALES")    
    private Long sales = 0L;

    @Column(name = "PRO_PROMO")    
    private Boolean promo = false;

    @Column(name = "PRO_DELETED")    
    private Boolean deleted = false;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(name = "SIZES_BY_PRODUCT_TBL",
        joinColumns =  {
            @JoinColumn(name = "SBP_PRO_ID", referencedColumnName = "PRO_ID")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "SBP_SIZE_ID", referencedColumnName = "SIZE_ID")
        })

    private Set<Size> sizes;

    public static Product findById(long id2) {
        return null;
    }

}
