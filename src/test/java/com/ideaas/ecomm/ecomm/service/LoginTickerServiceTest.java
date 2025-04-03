package com.ideaas.ecomm.ecomm.service;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.repository.LoginTicketDao;
import com.ideaas.ecomm.ecomm.services.LoginTickerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
    private LoginTickerService loginTicketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSave_ShouldReturnSavedTicket() {
        LoginTicketResponse loginTicket = new LoginTicketResponse();
        when(loginTicketDao.save(any(LoginTicketResponse.class))).thenReturn(loginTicket);

        LoginTicketResponse result = loginTicketService.save(loginTicket);

        assertNotNull(result, "El ticket guardado no debería ser nulo");
        assertEquals(loginTicket, result, "El ticket devuelto debería ser el mismo que el guardado");
        verify(loginTicketDao, times(1)).save(eq(loginTicket));
    }

    @Test
    void testGetActive_ShouldReturnActiveTicket() {
        String service = "testService";
        LoginTicketResponse loginTicket = new LoginTicketResponse();
        LocalDateTime testTime = LocalDateTime.now();

        when(loginTicketDao.getActive(any(LocalDateTime.class), eq(service)))
                .thenReturn(Optional.of(loginTicket));

        Optional<LoginTicketResponse> result = loginTicketService.getActive(service);

        assertTrue(result.isPresent(), "Se esperaba un ticket activo");
        assertEquals(loginTicket, result.get(), "El ticket devuelto no coincide con el esperado");

        ArgumentCaptor<LocalDateTime> timeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(loginTicketDao, times(1)).getActive(timeCaptor.capture(), eq(service));
        assertNotNull(timeCaptor.getValue(), "La fecha de consulta no debería ser nula");
    }

    @Test
    void testGetActive_ShouldReturnEmptyIfNoActiveTicket() {
        String service = "testService";

        when(loginTicketDao.getActive(any(LocalDateTime.class), eq(service)))
                .thenReturn(Optional.empty());

        Optional<LoginTicketResponse> result = loginTicketService.getActive(service);

        assertFalse(result.isPresent(), "Se esperaba un Optional vacío pero se encontró un valor");
        verify(loginTicketDao, times(1)).getActive(any(LocalDateTime.class), eq(service));
    }
}
