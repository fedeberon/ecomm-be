package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Stock;
import com.ideaas.ecomm.ecomm.repository.StockDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService implements IStockService {

    private StockDao dao;

    @Autowired
    public StockService(final StockDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Stock> findAll() {
        return dao.findAll();
    }

    @Override
    public Stock save(final Stock stock){
        return dao.save(stock);
    }

    @Override
    public void saveAll(final List<Stock> stock){
        dao.saveAll(stock);
    }


    @Override
    public List<Stock> getBy(final Product product){
        return dao.findAllByProduct(product);
    }

}
