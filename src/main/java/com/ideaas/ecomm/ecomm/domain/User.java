package com.ideaas.ecomm.ecomm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
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

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User  {

    @Id
    @Column(name = "USU_USERNAME")
    private String username;

    @JsonIgnore
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

    @Column(name = "USU_PHONE")
    private String phone;

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

}
