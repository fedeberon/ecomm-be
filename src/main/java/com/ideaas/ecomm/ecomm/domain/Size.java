package com.ideaas.ecomm.ecomm.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SIZE")
public class Size {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SIZE_ID")
    public Long id;

    @Column(name = "SIZE_NAME")
    private String name;

    @ManyToMany(mappedBy = "sizes", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Product> products;

    public Size update(Size size) {
        return null;
    }
}
