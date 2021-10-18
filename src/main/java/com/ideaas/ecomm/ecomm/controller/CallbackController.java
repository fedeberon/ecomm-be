package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Callback;
import com.ideaas.ecomm.ecomm.services.interfaces.ICallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("callback")
public class CallbackController {


    private ICallbackService callbackService;

    @Autowired
    public CallbackController(final ICallbackService callbackService) {
        this.callbackService = callbackService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Callback> get(@PathVariable Long id) {
        Callback callback = callbackService.get(id);

        return ResponseEntity.ok(callback);
    }

}
