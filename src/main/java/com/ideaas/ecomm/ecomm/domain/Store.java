package com.ideaas.ecomm.ecomm.domain;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.util.Set;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STORES")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STO_ID")
    private Long id;

    @Column(name = "STO_NAME")
    private String name;

    @Column(name = "STO_DESCR")
    private String description;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(name = "user_store", joinColumns = @JoinColumn(name = "store_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private Set<User> owners = new HashSet<>();

    @Column(name = "STO_EMAIL")
    private String email;

    @Column(name = "STO_TEL")
    private String telephone;

    @Column(name = "STO_ADDRESS")
    private String address;

    @OneToOne
    @JoinColumn(name = "STO_SCH_ID")
    private Schedule schedule;

    @Transient
    private Image logo;
}
