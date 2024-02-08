package com.ideaas.ecomm.ecomm.domain.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String password,
            name,
            lastName,
            cuit,
            email,
            phone,
            city,
            direction,
            postal,
            role;
}
