package com.ideaas.ecomm.ecomm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ideaas.ecomm.ecomm.domain.Provider;
import com.ideaas.ecomm.ecomm.exception.NotFoundException;
import com.ideaas.ecomm.ecomm.services.interfaces.IProvidersService;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping ("/provider")
public class ProvidersController {
    
    private IProvidersService providerService;
    
    @Autowired 
    public void ProviderController (final IProvidersService providerService ){
        this.providerService = providerService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Provider> get(@PathVariable final Long id){
        try {
            Provider provider = providerService.get(id);
            return ResponseEntity.ok().body(provider);
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Provider> list() {
        List<Provider> providers = providerService.findAll();
        return providers;
    }

    @PostMapping()
    public ResponseEntity<Provider> save (@RequestBody Provider provider){
        Provider providerSaved = providerService.save(provider);

        return ResponseEntity.accepted().body(providerSaved);
    }
}
