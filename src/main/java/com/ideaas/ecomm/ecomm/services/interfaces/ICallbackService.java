package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Callback;

public interface ICallbackService {
    Callback save(Callback callback);

    Callback get(Long id);
}
