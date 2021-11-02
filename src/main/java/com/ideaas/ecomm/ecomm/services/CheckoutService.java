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
    public Checkout get(final Long id) {
        Checkout checkout = dao.findById(id).get();
        checkout.getProducts().forEach(productToCart -> productService.addImagesOnProduct(productToCart.getProduct()));

        return checkout;
    }

    @Override
    public Checkout save(final CheckoutResponse response) {
        final Product product = productService.get(response.getProductId());
        Checkout checkout = dao.getCheckoutByCheckoutState(CheckoutState.IN_PROCESS);
        if (Objects.isNull(checkout)) {
            checkout = Checkout.builder().products(new ArrayList<>()).build();
        }
        final List<ProductToCart> products = checkout.getProducts();
        final ProductToCart productToCart = prepareProductToCart(product, checkout, response.getQuantity());
        products.add(productToCart);
        checkout.setProducts(products);
        checkout.setCheckoutState(CheckoutState.IN_PROCESS);
        dao.save(checkout);

        return checkout;
    }


    @Override
    public Checkout changeStateTo(final CheckoutState state, final Long checkoutId) {
        Checkout checkout = get(checkoutId);
        checkout.setCheckoutState(state);
        dao.save(checkout);

        return checkout;
    }

    public ProductToCart prepareProductToCart(final Product product, final Checkout checkout, final Integer quantity) {
        return  ProductToCart.builder()
                            .product(product)
                            .checkout(checkout)
                            .quantity(quantity)
                            .price(product.getPrice())
                            .build();
    }
}
