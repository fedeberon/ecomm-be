package com.ideaas.ecomm.ecomm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USU_USERNAME")
    private String username;

    @JsonIgnore
    @Column(name = "USU_PASSWORD")
    private String password;
}
