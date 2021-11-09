package com.ideaas.ecomm.ecomm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    private List<Detail> details;

    public Cart(CartBuilder builder) {
        this.details = builder.details;
    }

    public static class CartBuilder {

         private List<Detail> details;

         public CartBuilder withDetails(final List<Detail> details) {
             this.details = details;

             return this;
         }

         public Cart build(){
             return new Cart(this);
         }
    }

}


