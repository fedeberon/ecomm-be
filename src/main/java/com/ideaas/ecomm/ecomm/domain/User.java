package com.ideaas.ecomm.ecomm.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String username;
    private String name;
    private String password;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "users_rol_id")
    private Role role;
}
