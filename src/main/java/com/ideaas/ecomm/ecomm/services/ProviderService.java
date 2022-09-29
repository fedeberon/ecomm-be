package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Provider;
import com.ideaas.ecomm.ecomm.repository.ProviderDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IProvidersService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProviderService implements IProvidersService{
    
    private ProviderDao dao;

    @Autowired
    public ProviderService(final ProviderDao dao){
        this.dao= dao;
    }

    @Override
    public Provider save(Provider provider) {
        return dao.save(provider);
    }

    @Override
    public Provider get(final Long id) {
        Optional<Provider> optionalProvider = dao.findById(id);

        return optionalProvider.get();
    }

    @Override
    public List<Provider> findAll() {
        List<Provider> providers = (List<Provider>) dao.findAll();
        return providers;
    }


}
