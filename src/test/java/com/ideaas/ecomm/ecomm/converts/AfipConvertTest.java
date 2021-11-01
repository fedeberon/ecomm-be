package com.ideaas.ecomm.ecomm.converts;

import com.ideaas.ecomm.ecomm.payload.LoginTicket;
import com.ideaas.ecomm.ecomm.util.FileUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AfipConvertTest {


    @Test
    public void shouldConvertToLoginTicketResponse() throws IOException {
        File resource = FileUtil.loadEmployeesWithSpringInternalClass("files/LoginTicketResponse.xml");
        String loginTicketResponse = new String(Files.readAllBytes(resource.toPath()));
        LoginTicket loginTicket = AfipConvert.convertToLoginTicketResponse(loginTicketResponse);

        assertNotNull(loginTicket.getCredential().getToken());
        assertNotNull(loginTicket.getCredential().getSign());

    }

}
