package com.ideaas.ecomm.ecomm.converts;

import com.ideaas.ecomm.ecomm.payload.BillResponse;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;
import com.ideaas.ecomm.ecomm.payload.LoginTicket;
import com.ideaas.ecomm.ecomm.util.FileUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

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


    @Test
    public void shouldConvertToLastBillId() throws IOException {
        File resource = FileUtil.loadEmployeesWithSpringInternalClass("files/GetLastBillId.xml");
        String loginTicketResponse = new String(Files.readAllBytes(resource.toPath()));
        loginTicketResponse = loginTicketResponse.replace("soap:", "");
        loginTicketResponse = loginTicketResponse.replace("xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"", "");
        loginTicketResponse = loginTicketResponse.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
        loginTicketResponse = loginTicketResponse.replace("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"", "");
        loginTicketResponse = loginTicketResponse.replace("xmlns=\"http://ar.gov.afip.dif.FEV1/\"", "");
        loginTicketResponse = loginTicketResponse.replace("xmlns=\"http://ar.gov.afip.dif.FEV1/\"", "");
        LastBillIdResponse loginTicket = AfipConvert.convertoToLastBillId(loginTicketResponse);

        assertNotNull(loginTicket.getLastId());

    }

    @Test
    public void shouldConvertToBill() throws IOException {
        File resource = FileUtil.loadEmployeesWithSpringInternalClass("files/Bill.xml");
        String xml = new String(Files.readAllBytes(resource.toPath()));
        BillResponse bill = AfipConvert.convertoToBillResponse(xml);

        assertNotNull(bill.getCAE());
    }


    @Test
    @Disabled
    @Ignore
    public void shouldConvertToBillResponseWithErrors() throws IOException {
        File resource = FileUtil.loadEmployeesWithSpringInternalClass("files/BillResponseWithErrors.xml");
        String xml = new String(Files.readAllBytes(resource.toPath()));
        BillResponse bill = AfipConvert.convertoToBillResponse(xml);

        assertNotNull(bill.getMsg());
    }

    @Test
    public void shouldConvertToBillResponseWithObservations() throws IOException {
        File resource = FileUtil.loadEmployeesWithSpringInternalClass("files/BillResponseWithObservations.xml");
        String xml = new String(Files.readAllBytes(resource.toPath()));
        BillResponse bill = AfipConvert.convertoToBillResponse(xml);

        assertNotNull(bill.getMessage());
    }

}
