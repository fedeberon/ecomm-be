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
import org.mockito.Mockito;
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

    private AfipService spyAfipService;

    @BeforeEach
    void setUp() {
        spyAfipService = Mockito.spy(afipService);
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
    void testGet_WhenLoginTicketDoesNotExist() {
        String service = "testService";
        LoginTicketResponse mockedTicket = new LoginTicketResponse();

        when(loginTicketService.getActive(service)).thenReturn(Optional.empty());
        doReturn(mockedTicket).when(spyAfipService).getAuthentication(service);
        when(loginTicketService.save(mockedTicket)).thenReturn(mockedTicket);

        LoginTicketResponse actualResponse = spyAfipService.get(service);

        assertNotNull(actualResponse);
        verify(loginTicketService, times(1)).getActive(service);
        verify(spyAfipService, times(1)).getAuthentication(service);
        verify(loginTicketService, times(1)).save(mockedTicket);
    }
}

