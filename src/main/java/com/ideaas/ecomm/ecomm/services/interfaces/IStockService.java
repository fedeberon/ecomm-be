package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Stock;

import java.util.List;

public interface IStockService {
    List<Stock> findAll();

    Stock save(Stock stock);

    void saveAll(List<Stock> stock);

    List<Stock> getBy(Product product);
}
