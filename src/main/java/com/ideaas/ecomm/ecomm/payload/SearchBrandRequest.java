package com.ideaas.ecomm.ecomm.payload;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchBrandRequest {

    private List<BrandRequest> brandRequests;

    @Getter
    public static class BrandRequest {
        private Long id;
        private boolean active;

    }



    @Getter
    public static class CategoriesRequest {
        private Long id; 
    }
}
