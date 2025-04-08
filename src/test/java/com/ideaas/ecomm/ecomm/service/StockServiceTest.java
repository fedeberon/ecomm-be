package com.ideaas.ecomm.ecomm.service;
import com.ideaas.ecomm.ecomm.domain.ItemStock;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Provider;
import com.ideaas.ecomm.ecomm.domain.Stock;
import com.ideaas.ecomm.ecomm.repository.StockDao;
import com.ideaas.ecomm.ecomm.services.ProductService;
import com.ideaas.ecomm.ecomm.services.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class StockServiceTest {

    private StockDao stockDao;
    private ProductService productService;
    private StockService stockService;

    @BeforeEach
    void setUp() {
        stockDao = mock(StockDao.class);
        productService = mock(ProductService.class);
        stockService = new StockService(stockDao, productService);
    }

    @Test
    void save_ShouldSetStockInItemsAndCallDaoSave() {
        Stock stock = new Stock();
        stock.setProvider(new Provider());
        ItemStock item1 = new ItemStock();
        ItemStock item2 = new ItemStock();

        stock.setItems(Arrays.asList(item1, item2));

        stockService.save(stock);

        assertEquals(stock, item1.getStock());
        assertEquals(stock, item2.getStock());

        verify(stockDao).save(stock);
    }

    @Test
    void save_ShouldWorkWithEmptyItemsList() {
        Stock stock = new Stock();
        stock.setProvider(new Provider());
        stock.setItems(Collections.emptyList());

        assertDoesNotThrow(() -> stockService.save(stock));

        verify(stockDao).save(stock);
    }

    @Test
    void save_ShouldWorkWhenItemsIsNull() {
        Stock stock = new Stock();
        stock.setProvider(new Provider());
        stock.setItems(null);

        assertDoesNotThrow(() -> stockService.save(stock));
        verify(stockDao).save(stock);
    }

    @Test
    void save_ShouldThrowExceptionWhenStockIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> stockService.save(null));
        assertEquals("The stock cannot be zero", exception.getMessage());
        verify(stockDao, never()).save(any());
    }

    @Test
    void findAll_ShouldReturnAllStocks() {
        List<Stock> expectedStocks = Arrays.asList(new Stock(), new Stock());
        when(stockDao.findAll()).thenReturn(expectedStocks);

        List<Stock> result = stockService.findAll();

        assertEquals(expectedStocks, result);
        verify(stockDao).findAll();
    }

    @Test
    void getByProduct_ShouldReturnStock() {
        Product product = new Product();
        Stock expectedStock = new Stock();
        when(stockDao.findAllByItemsProduct(product)).thenReturn(expectedStock);

        Stock result = stockService.getBy(product);

        assertEquals(expectedStock, result);
        verify(stockDao).findAllByItemsProduct(product);
    }

    @Test
    void getById_ShouldReturnStock() {
        Long id = 1L;
        Stock expectedStock = new Stock();
        when(stockDao.findById(id)).thenReturn(Optional.of(expectedStock));

        Stock result = stockService.getBy(id);

        assertEquals(expectedStock, result);
        verify(stockDao).findById(id);
    }

    @Test
    void getById_ShouldThrowWhenNotFound() {
        Long id = 1L;
        when(stockDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> stockService.getBy(id));
        verify(stockDao).findById(id);
    }
}
