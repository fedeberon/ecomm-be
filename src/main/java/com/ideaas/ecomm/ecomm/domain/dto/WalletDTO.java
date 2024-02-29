package com.ideaas.ecomm.ecomm.domain.dto;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.Wallet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class WalletDTO {
    private Long id, points;
    private Integer quantity;
    private User user;
    private LocalDateTime date;
    private Product product;
    private Boolean isConsumed;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class User {
        private String username;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Product {
        private Long id;
        private String name;
    }
}
