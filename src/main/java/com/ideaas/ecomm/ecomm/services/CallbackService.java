package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Callback;
import com.ideaas.ecomm.ecomm.repository.CallbackDao;
import com.ideaas.ecomm.ecomm.services.interfaces.ICallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallbackService implements ICallbackService {

    private CallbackDao dao;

    @Autowired
    public CallbackService(CallbackDao dao) {
        this.dao = dao;
    }

    @Override
    public Callback save(final Callback callback) {
        return dao.save(callback);
    }

    @Override
    public Callback get(final Long id) {
        return dao.findById(id).get();
    }

}
