package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Stock;

import java.util.List;

public interface IStockService {
    
    List<Stock> findAll();

    void save(Stock stock);

    Stock getBy(Product product);

    Stock getBy(Long id);
}
