package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Cart;
import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.domain.Size;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import com.ideaas.ecomm.ecomm.repository.CheckoutDao;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import com.ideaas.ecomm.ecomm.services.interfaces.ISizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CheckoutService implements ICheckoutService {

    private CheckoutDao dao;
    private IProductService productService;

    private ISizeService sizeService;

    @Autowired
    public CheckoutService(final CheckoutDao dao,
                           final IProductService productService,
                           final ISizeService sizeService) {
        this.dao = dao;
        this.productService = productService;
        this.sizeService = sizeService;
    }

    @Override
    public Checkout get(final Long id) {
        Checkout checkout = dao.findById(id).get();
        checkout.getProducts().forEach(productToCart -> productService.addImagesOnProduct(productToCart.getProduct()));

        return checkout;
    }

    @Override
    public Checkout save(final Cart cart, CheckoutState state) {
        final List<Product> products = new ArrayList<>();
        final List<ProductToCart> productsToCart = new ArrayList<>();
        Checkout checkout = Checkout.builder()
                .checkoutState(state)
                .products(productsToCart)
                .build();
        cart.getDetails().forEach(detail -> {
           Product product = productService.get(Long.valueOf(detail.getProductId()));
           Size size = sizeService.get(Objects.nonNull(detail.getSize()) ? Long.valueOf(detail.getSize()) : 0);
           products.add(product);
           final ProductToCart productToCart = prepareProductToCart(product, checkout, detail.getQuantity(), size);
           productsToCart.add(productToCart);
        });

        dao.save(checkout);

        return checkout;
    }

    @Override
    public Page<Checkout> findAll(Pageable pageable) {
        return dao.findAll(pageable);
    }

    @Override
    public Checkout changeStateTo(final CheckoutState state, final Long checkoutId) {
        Checkout checkout = get(checkoutId);
        checkout.setCheckoutState(state);
        dao.save(checkout);

        return checkout;
    }

    public static ProductToCart prepareProductToCart(final Product product,
                                              final Checkout checkout,
                                              final Integer quantity,
                                              final Size size) {
        return  ProductToCart.builder()
                            .product(product)
                            .checkout(checkout)
                            .quantity(quantity)
                            .price(product.getPrice())
                            .size(size)
                            .build();
    }
}
