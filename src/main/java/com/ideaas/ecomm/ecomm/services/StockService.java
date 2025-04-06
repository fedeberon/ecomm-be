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
    private ProductService productService;

    @Autowired
    public StockService(final StockDao dao, final ProductService productService) {
        this.dao = dao;
        this.productService = productService;
    }

    @Override
    public List<Stock> findAll() {
        return dao.findAll();
    }

    @Override
    public void save(final Stock stock){
        if (stock == null) {
            throw new IllegalArgumentException("The stock cannot be zero");
        }
        if (stock.getItems() != null) {
            stock.getItems().forEach(itemStock -> itemStock.setStock(stock));
        }
        dao.save(stock);
    }


    @Override
    public Stock getBy(final Product product){
        return dao.findAllByItemsProduct(product);
    }


    @Override
    public Stock getBy(final Long id){
        return dao.findById(id).get();
    }


}
