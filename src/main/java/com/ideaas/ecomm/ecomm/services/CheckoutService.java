package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import com.ideaas.ecomm.ecomm.payload.CheckoutResponse;
import com.ideaas.ecomm.ecomm.repository.CheckoutDao;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CheckoutService implements ICheckoutService {

    private CheckoutDao dao;
    private IProductService productService;

    @Autowired
    public CheckoutService(final CheckoutDao dao, final IProductService productService) {
        this.dao = dao;
        this.productService = productService;
    }

    @Override
    public Checkout save(final CheckoutResponse response) {
        Product product = productService.get(response.getProductId());
        Checkout checkout = dao.getCheckoutByState(CheckoutState.ACTIVE);
        if (Objects.isNull(checkout)) {
            checkout = Checkout.builder().products(new ArrayList<>()).build();
        }
        List<ProductToCart> products = checkout.getProducts();
        ProductToCart productToCart = prepareProductToCart(product, checkout, response.getQuantity());
        products.add(productToCart);
        checkout.setProducts(products);
        checkout.setState(CheckoutState.ACTIVE);
        dao.save(checkout);

        return checkout;
    }



    public ProductToCart prepareProductToCart(final Product product, final Checkout checkout, final Long quantity) {
        return  ProductToCart.builder()
                            .product(product)
                            .checkout(checkout)
                            .quantity(quantity)
                            .build();
    }
}
