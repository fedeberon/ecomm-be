package com.ideaas.ecomm.ecomm.service;

import com.ideaas.ecomm.ecomm.domain.Brand;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.repository.BrandDao;
import com.ideaas.ecomm.ecomm.services.BrandService;
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
public class BrandServiceTest {

        @Mock
        private BrandDao brandDao;

        @Mock
        private IProductService productService;

        @InjectMocks
        private BrandService brandService;

        private Brand brand;

        @BeforeEach
        void setUp() {
            brand = new Brand();
            brand.setId(1L);
            brand.setName("Nike");
        }

        @Test
        void testFindAll() {
            List<Brand> brands = Arrays.asList(brand);
            when(brandDao.findAll()).thenReturn(brands);

            List<Brand> result = brandService.findAll();

            assertEquals(1, result.size());
            assertEquals("Nike", result.get(0).getName());
            verify(brandDao, times(1)).findAll();
        }

        @Test
        void testFindById_WhenBrandExists() {
            when(brandDao.findById(1L)).thenReturn(Optional.of(brand));

            Brand result = brandService.findById(1L);

            assertNotNull(result);
            assertEquals(Long.valueOf(1L), result.getId());
            verify(brandDao, times(1)).findById(1L);
        }

        @Test
        void testFindById_WhenBrandDoesNotExist() {
            when(brandDao.findById(2L)).thenReturn(Optional.empty());

            Brand result = brandService.findById(2L);

            assertNull(result);
            verify(brandDao, times(1)).findById(2L);
        }

        @Test
        void testSave() {
            when(brandDao.save(brand)).thenReturn(brand);

            Brand result = brandService.save(brand);

            assertNotNull(result);
            assertEquals("Nike", result.getName());
            verify(brandDao, times(1)).save(brand);
        }

        @Test
        void testUpdate() {
            when(brandDao.save(brand)).thenReturn(brand);

            Brand result = brandService.update(brand);

            assertNotNull(result);
            assertEquals("Nike", result.getName());
            verify(brandDao, times(1)).save(brand);
        }

        @Test
        void testGet_WhenBrandExists() {
            when(brandDao.findById(1L)).thenReturn(Optional.of(brand));

            Brand result = brandService.get(1L);

            assertNotNull(result);
            assertEquals(Long.valueOf(1L), result.getId());
            verify(brandDao, times(1)).findById(1L);
        }

        @Test
        void testDelete_WhenBrandHasProducts() {
            Product product = new Product();
            product.setId(10L);
            product.setBrand(brand);

            List<Product> productList = Arrays.asList(product);

            when(productService.findAll()).thenReturn(productList);
            doNothing().when(brandDao).delete(brand);

            brandService.delete(brand);

            assertNull(product.getBrand());
            verify(productService, times(1)).findAll();
            verify(brandDao, times(1)).delete(brand);
        }

        @Test
        void testDelete_WhenBrandHasNoProducts() {
            List<Product> emptyList = Arrays.asList();

            when(productService.findAll()).thenReturn(emptyList);
            doNothing().when(brandDao).delete(brand);

            brandService.delete(brand);

            verify(productService, times(1)).findAll();
            verify(brandDao, times(1)).delete(brand);
        }

        @Test
        void testFindProductsInBrand() {
            Product product = new Product();
            product.setId(10L);
            product.setBrand(brand);

            List<Product> productList = Arrays.asList(product);
            when(brandDao.findById(1L)).thenReturn(Optional.of(brand));
            when(productService.findAll()).thenReturn(productList);

            List<Product> result = brandService.findProductsInBrand(1L);

            assertEquals(1, result.size());
            assertEquals(Long.valueOf(10L), result.get(0).getId());
            verify(brandDao, times(1)).findById(1L);
            verify(productService, times(1)).findAll();
        }
}


