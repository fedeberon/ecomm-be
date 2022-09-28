package com.ideaas.ecomm.ecomm.services.interfaces;

import java.util.List;
import com.ideaas.ecomm.ecomm.domain.Provider;

public interface IProvidersService {

    Provider save(Provider provider);

    Provider get(Long id);

    List<Provider> findAll();

}
