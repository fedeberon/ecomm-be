package com.ideaas.ecomm.ecomm.payload;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchRequest {

    private List<BrandRequest> brandRequests;

    private List<CategoriesRequest> categoriesRequests;

    private int page;

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
