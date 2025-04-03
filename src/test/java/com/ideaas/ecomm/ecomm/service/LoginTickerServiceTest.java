package com.ideaas.ecomm.ecomm.service;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.repository.LoginTicketDao;
import com.ideaas.ecomm.ecomm.services.LoginTickerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class LoginTickerServiceTest {
    @Mock
    private LoginTicketDao loginTicketDao;

    @InjectMocks
    private LoginTickerService loginTickerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void testSave_ShouldReturnSavedTicket() {
        LoginTicketResponse loginTicket = new LoginTicketResponse();
        when(loginTicketDao.save(loginTicket)).thenReturn(loginTicket);

        LoginTicketResponse result = loginTickerService.save(loginTicket);

        assertNotNull(result);
        assertEquals(loginTicket, result);
        verify(loginTicketDao, times(1)).save(loginTicket);
    }

    @Test
    void testGetActive_ShouldReturnActiveTicket() {
        String service = "testService";
        LoginTicketResponse loginTicket = new LoginTicketResponse();
        when(loginTicketDao.getActive(any(LocalDateTime.class), eq(service))).thenReturn(Optional.of(loginTicket));

        Optional<LoginTicketResponse> result = loginTickerService.getActive(service);

        assertTrue(result.isPresent());
        assertEquals(loginTicket, result.get());
        verify(loginTicketDao, times(1)).getActive(any(LocalDateTime.class), eq(service));
    }

    @Test
    void testGetActive_ShouldReturnEmptyIfNoActiveTicket() {
        String service = "testService";
        when(loginTicketDao.getActive(any(LocalDateTime.class), eq(service))).thenReturn(Optional.empty());

        Optional<LoginTicketResponse> result = loginTickerService.getActive(service);

        assertFalse(result.isPresent());
        verify(loginTicketDao, times(1)).getActive(any(LocalDateTime.class), eq(service));
    }
}
