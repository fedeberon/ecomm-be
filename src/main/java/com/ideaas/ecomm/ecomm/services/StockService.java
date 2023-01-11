package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Stock;
import com.ideaas.ecomm.ecomm.repository.StockDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    public Stock save(final Stock stock){
        return dao.save(stock);
    }

    @Override
    public List<Stock> saveAll(final List<Stock> stocks){
        stocks.forEach(stock ->{
            Stock newStock = dao.save(stock);
            Long quantityToAdd = newStock.getQuantity();
            Product product = newStock.getProduct();
            Long newQuantity = Objects.nonNull(product.getStock()) ? quantityToAdd + product.getStock() : quantityToAdd;
            product.setStock(newQuantity);
            productService.save(product);
        });
        return dao.saveAll(stocks);
    }


    @Override
    public List<Stock> getBy(final Product product){
        return dao.findAllByProduct(product);
    }

}
