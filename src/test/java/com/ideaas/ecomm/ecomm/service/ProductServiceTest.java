package com.ideaas.ecomm.ecomm.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ideaas.ecomm.ecomm.domain.*;
import com.ideaas.ecomm.ecomm.repository.ProductDao;
import com.ideaas.ecomm.ecomm.services.FileService;
import com.ideaas.ecomm.ecomm.services.ProductService;
import com.ideaas.ecomm.ecomm.services.interfaces.ICategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private FileService fileService;

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("Laptop de gama alta")
                .price(1200.0)
                .stock(10L)
                .deleted(false)
                .build();
    }

    @Test
    void testFindAll() {
        List<Product> products = Arrays.asList(product);
        when(productDao.findByDeleted(false)).thenReturn(products);
        List<Product> result = productService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
    }

    @Test
    void testFindAllWithPagination() {
        Page<Product> page = new PageImpl<>(Collections.singletonList(product));
        when(productDao.findByDeleted(eq(false), any(Pageable.class))).thenReturn(page);
        Page<Product> result = productService.findAll(0, 10, "name");
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testSave() {
        when(productDao.save(product)).thenReturn(product);
        Product savedProduct = productService.save(product);
        assertNotNull(savedProduct);
        assertEquals("Laptop", savedProduct.getName());
    }

    @Test
    void testGetProductById() {
        when(productDao.findById(1L)).thenReturn(Optional.of(product));
        Product result = productService.get(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testDeleteProduct() {
        when(productDao.findById(1L)).thenReturn(Optional.of(product));
        when(productDao.save(any(Product.class))).thenReturn(product);
        Product deletedProduct = productService.deleteProduct(1L);
        assertTrue(deletedProduct.getDeleted());
    }

    @Test
    void testActivateProduct() {
        product.setDeleted(true);
        when(productDao.findById(1L)).thenReturn(Optional.of(product));
        when(productDao.save(any(Product.class))).thenReturn(product);
        Product activatedProduct = productService.activateProduct(1L);
        assertFalse(activatedProduct.getDeleted());
    }
}
