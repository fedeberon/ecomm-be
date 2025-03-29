package com.ideaas.ecomm.ecomm.service;

import com.ideaas.ecomm.ecomm.domain.Category;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.repository.CategoryDao;
import com.ideaas.ecomm.ecomm.services.CategoryService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryDao categoryDao;

    @Mock
    private IProductService productService;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
    }

    @Test
    void testFindAll() {
        List<Category> categories = Arrays.asList(category);
        when(categoryDao.findAll()).thenReturn(categories);

        List<Category> result = categoryService.findAll();

        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
        verify(categoryDao, times(1)).findAll();
    }

    @Test
    void testFindAllByNameEquals_WhenCategoryExists() {
        when(categoryDao.findAllByNameEquals("Electronics")).thenReturn(Optional.of(category));

        Category result = categoryService.findAllByNameEquals("Electronics");

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryDao, times(1)).findAllByNameEquals("Electronics");
    }

    @Test
    void testFindAllByNameEquals_WhenCategoryDoesNotExist() {
        when(categoryDao.findAllByNameEquals("Clothing")).thenReturn(Optional.empty());

        Category result = categoryService.findAllByNameEquals("Clothing");

        assertNull(result);
        verify(categoryDao, times(1)).findAllByNameEquals("Clothing");
    }

    @Test
    void testFindById_WhenCategoryExists() {
        when(categoryDao.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        verify(categoryDao, times(1)).findById(1L);
    }

    @Test
    void testFindById_WhenCategoryDoesNotExist() {
        when(categoryDao.findById(2L)).thenReturn(Optional.empty());

        Category result = categoryService.findById(2L);

        assertNull(result);
        verify(categoryDao, times(1)).findById(2L);
    }

    @Test
    void testSave() {
        when(categoryDao.save(category)).thenReturn(category);

        Category result = categoryService.save(category);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryDao, times(1)).save(category);
    }

    @Test
    void testDelete_WhenCategoryHasProducts() {
        Product product = new Product();
        product.setId(10L);
        product.setCategory(category);

        List<Product> productList = Arrays.asList(product);

        when(productService.findAll()).thenReturn(productList);
        doNothing().when(categoryDao).delete(category);

        categoryService.delete(category);

        assertNull(product.getCategory());
        verify(productService, times(1)).findAll();
        verify(categoryDao, times(1)).delete(category);
    }

    @Test
    void testDelete_WhenCategoryHasNoProducts() {
        List<Product> emptyList = Arrays.asList();

        when(productService.findAll()).thenReturn(emptyList);
        doNothing().when(categoryDao).delete(category);

        categoryService.delete(category);

        verify(productService, times(1)).findAll();
        verify(categoryDao, times(1)).delete(category);
    }
}
