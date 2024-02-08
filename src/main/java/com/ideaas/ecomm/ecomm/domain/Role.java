package com.ideaas.ecomm.ecomm.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ROLES")
public class Role implements GrantedAuthority {

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
        return this.role.toString();
    }
}
