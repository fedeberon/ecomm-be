package com.ideaas.ecomm.ecomm.service;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.Size;
import com.ideaas.ecomm.ecomm.repository.SizeDao;
import com.ideaas.ecomm.ecomm.services.SizeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SizeServiceTest {

    @Mock
    private SizeDao sizeDao;

    @InjectMocks
    private SizeService sizeService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave_ShouldReturnSavedSize() {
        Size size = new Size();
        size.setId(1L);
        size.setName("XL");

        when(sizeDao.save(size)).thenReturn(size);

        Size result = sizeService.save(size);

        assertNotNull(result);
        assertEquals("XL", result.getName());
        verify(sizeDao, times(1)).save(size);
    }

    @Test
    public void testGet_ShouldReturnSizeById() {
        Long id = 1L;
        Size size = new Size();
        size.setId(id);
        size.setName("M");

        when(sizeDao.findById(id)).thenReturn(Optional.of(size));

        Size result = sizeService.get(id);

        assertNotNull(result);
        assertEquals("M", result.getName());
        verify(sizeDao, times(1)).findById(id);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGet_ShouldThrowException_WhenSizeNotFound() {
        Long id = 99L;

        when(sizeDao.findById(id)).thenReturn(Optional.empty());
        sizeService.get(id); // debe lanzar excepción
    }

    @Test
    public void testFindAll_ShouldReturnAllSizes() {
        Size s1 = new Size();
        s1.setId(1L);
        s1.setName("S");

        Size s2 = new Size();
        s2.setId(2L);
        s2.setName("M");

        List<Size> sizeList = Arrays.asList(s1, s2);
        when(sizeDao.findAll()).thenReturn(sizeList);

        List<Size> result = sizeService.findAll();

        assertEquals(2, result.size());
        assertEquals("S", result.get(0).getName());
        assertEquals("M", result.get(1).getName());
        verify(sizeDao, times(1)).findAll();
    }

    @Test
    public void testDelete_ShouldRemoveSizeFromProductsAndCallDao() {
        Size sizeToDelete = new Size();
        sizeToDelete.setId(1L);
        sizeToDelete.setName("M");

        Size anotherSize = new Size();
        anotherSize.setId(2L);
        anotherSize.setName("L");

        Product product = new Product();
        Set<Size> productSizes = new HashSet<>();
        productSizes.add(sizeToDelete);
        productSizes.add(anotherSize);
        product.setSizes(productSizes);

        Set<Product> relatedProducts = new HashSet<>();
        relatedProducts.add(product);
        sizeToDelete.setProducts(relatedProducts);

        sizeService.delete(sizeToDelete);

        assertFalse(product.getSizes().contains(sizeToDelete));
        verify(sizeDao, times(1)).delete(sizeToDelete);
    }
}
