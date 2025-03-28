package com.ideaas.ecomm.ecomm.service;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.services.AfipService;
import com.ideaas.ecomm.ecomm.services.interfaces.ILoginTicketService;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AfipServiceTest {

    @Mock
    private ILoginTicketService loginTicketService;

    @InjectMocks
    private AfipService afipService;

    @BeforeEach
    void setUp() {
        // Configurar mocks si es necesario antes de cada prueba
    }

    @Test
    void testGet_WhenLoginTicketExists() {
        String service = "testService";
        LoginTicketResponse expectedResponse = new LoginTicketResponse();
        when(loginTicketService.getActive(service)).thenReturn(Optional.of(expectedResponse));

        LoginTicketResponse actualResponse = afipService.get(service);

        assertEquals(expectedResponse, actualResponse);
        verify(loginTicketService, times(1)).getActive(service);
        verifyNoMoreInteractions(loginTicketService);
    }

    @Test
    @Disabled
    void testGet_WhenLoginTicketDoesNotExist() {
        String service = "testService";
        LoginTicketResponse newResponse = new LoginTicketResponse();
        when(loginTicketService.getActive(service)).thenReturn(Optional.empty());
        when(loginTicketService.save(any())).thenReturn(newResponse);

        LoginTicketResponse actualResponse = afipService.get(service);

        assertNotNull(actualResponse);
        verify(loginTicketService, times(1)).getActive(service);
        verify(loginTicketService, times(1)).save(any(LoginTicketResponse.class));
    }
}

