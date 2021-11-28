package com.ideaas.ecomm.ecomm.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ROLES")
public class Role implements GrantedAuthority {

    /**
     * Roles
     */
    public final static String ROLE_USER = "ROLE_USER";
    public final static String ROLE_PROFESSIONAL = "ROLE_PROFESSIONAL";
    public final static String ROLE_VISITOR = "ROLE_VISITOR";
    public final static String ROLE_TWO_FACTOR_AUTHENTICATION = "ROLE_TWO_FACTOR_AUTHENTICATION";
    public final static String ROLE_ADMINISTRATOR = "ROLE_ADMINISTRATOR";
    public final static String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    public final static String ROLE_COMPANY = "ROLE_COMPANY";

    @Id
    @GeneratedValue
    @Column(name = "ROLE_ID", nullable = false)
    private Long id;

    @Column(name = "ROLE_ROLE")
    private String role;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Override
    public String getAuthority() {
        return this.role;
    }
}
