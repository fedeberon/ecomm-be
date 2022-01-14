package com.ideaas.ecomm.ecomm.repository;

import java.util.List;

import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDao extends JpaRepository<Stock, Long> {

    List<Stock> findAllByProduct(final Product product);
}
