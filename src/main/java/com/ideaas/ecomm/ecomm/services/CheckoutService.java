package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Cart;
import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import com.ideaas.ecomm.ecomm.repository.CheckoutDao;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public Checkout save(final Cart cart) {
        final List<Product> products = new ArrayList<>();
        final List<ProductToCart> productsToCart = new ArrayList<>();
        final Checkout checkout = new Checkout();
        cart.getDetails().forEach(detail -> {
           Product product = productService.get(Long.valueOf(detail.getProductId()));
           products.add(product);
            final ProductToCart productToCart = prepareProductToCart(product, checkout, detail.getQuantity());
            productsToCart.add(productToCart);
        });
        checkout.setProducts(productsToCart);
        checkout.setCheckoutState(CheckoutState.IN_PROCESS);
        dao.save(checkout);

        return checkout;
    }

    @Override
    public List<Checkout> findAll() {
        return dao.findAll(Sort.by(Sort.Direction.DESC, "id"));
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
