package com.ideaas.ecomm.ecomm.service;

import com.ideaas.ecomm.ecomm.domain.Provider;
import com.ideaas.ecomm.ecomm.repository.ProviderDao;
import com.ideaas.ecomm.ecomm.services.ProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProviderServiceTest {

    @Mock
    private ProviderDao providerDao;

    @InjectMocks
    private ProviderService providerService;

    @BeforeEach
    void setUp() {
        // Mockito 2.24.0 usa initMocks
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSave_ShouldReturnSavedProvider() {
        Provider provider = new Provider();
        when(providerDao.save(provider)).thenReturn(provider);

        Provider result = providerService.save(provider);

        assertNotNull(result);
        assertEquals(provider, result);
        verify(providerDao, times(1)).save(provider);
    }
    @Test
    void testSave_ShouldReturnSavedProvider_WithData() {
        Provider provider = new Provider();
        provider.setId(1L);
        provider.setName("Proveedor S.A.");
        provider.setCuit("30-12345678-9");
        provider.setAddress("Av. Siempre Viva 742");

        when(providerDao.save(provider)).thenReturn(provider);

        Provider result = providerService.save(provider);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Proveedor S.A.", result.getName());
        assertEquals("30-12345678-9", result.getCuit());
        assertEquals("Av. Siempre Viva 742", result.getAddress());

        verify(providerDao, times(1)).save(provider);
    }

    @Test
    void testGet_ShouldReturnProviderById() {
        Long id = 1L;
        Provider provider = new Provider();
        when(providerDao.findById(id)).thenReturn(Optional.of(provider));

        Provider result = providerService.get(id);

        assertNotNull(result);
        assertEquals(provider, result);
        verify(providerDao, times(1)).findById(id);
    }

    @Test
    void testGet_ShouldThrowExceptionIfProviderNotFound() {
        Long id = 1L;
        when(providerDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            providerService.get(id);
        });

        verify(providerDao, times(1)).findById(id);
    }

    @Test
    void testFindAll_ShouldReturnListOfProviders() {
        Provider provider1 = new Provider();
        Provider provider2 = new Provider();
        List<Provider> expectedList = Arrays.asList(provider1, provider2);

        when(providerDao.findAll()).thenReturn(expectedList);

        List<Provider> result = providerService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedList, result);
        verify(providerDao, times(1)).findAll();
    }
}
