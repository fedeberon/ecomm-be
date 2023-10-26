package com.ideaas.ecomm.ecomm.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USERS")
public class User  {

    @Id
    @Column(name = "USU_USERNAME")
    private String username;

    @Column(name = "USU_PASSWORD")
    private String password;

    @Column(name = "USU_NAME")
    private String name;

    @Column(name = "USU_LAST_NAME")
    private String lastName;

    @Column(name = "USU_CARD_ID")
    private String cardId;

    @Column(name = "USU_CUIT")
    private String cuit;

    @Column(name = "USU_EMAIL")
    private String email;

    @Column(name = "USU_PHONE")
    private String phone;

    @Column(name = "USU_CITY")
    private String city;

    @Column(name = "USU_DIRECTION")
    private String direction;

    @Column(name = "USU_POSTAL")
    private String postal;

    @Column(name = "USU_MELLIZOS")
    private Boolean twins;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Role> roles;

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role: this.roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return authorities;

    }

    public User(String username) {
        this.username = username;
    }

}
