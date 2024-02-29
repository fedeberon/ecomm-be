package com.ideaas.ecomm.ecomm.domain.dto;

import com.ideaas.ecomm.ecomm.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private String
            username,
            password,
            name,
            lastName,
            cuit,
            phone,
            city,
            direction,
            postal,
            role;
}
