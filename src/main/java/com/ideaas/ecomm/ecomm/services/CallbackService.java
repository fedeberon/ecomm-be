package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Callback;
import com.ideaas.ecomm.ecomm.repository.CallbackDao;
import com.ideaas.ecomm.ecomm.services.interfaces.ICallbackService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallbackService implements ICallbackService {

    private CallbackDao dao;
    private IProductService productService;

    @Autowired
    public CallbackService(final CallbackDao dao,
                           final IProductService productService) {
        this.dao = dao;
        this.productService = productService;
    }

    @Override
    public Callback save(final Callback callback) {
        return dao.save(callback);
    }

    @Override
    public Callback get(final Long id) {
        Callback callback = dao.findById(id).get();
        callback.getCheckout().getProducts()
                .forEach(productToCart -> productService.addImagesOnProduct(productToCart.getProduct()));

        return callback;
    }

}
