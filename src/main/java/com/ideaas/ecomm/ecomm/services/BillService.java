package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.payload.BillRequest;
import com.ideaas.ecomm.ecomm.payload.BillResponse;
import com.ideaas.ecomm.ecomm.payload.CAEAResponse;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.Person;
import com.ideaas.ecomm.ecomm.payload.PersonPayload;
import com.ideaas.ecomm.ecomm.services.interfaces.IBillService;
import org.springframework.stereotype.Service;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;

import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertToCAE;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertToPersonPayload;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertoToBillResponse;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertoToLastBillId;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.printSOAPResponse;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createBill;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createGetCAE;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createGetLastBillId;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createSOAPRequest;

@SuppressWarnings("all")
@Service
public class BillService implements IBillService {

    public static String AFIP_A5_SERVICE   = "https://aws.afip.gov.ar/sr-padron/webservices/personaServiceA5";
    public static String AFIP_CAE          = "https://fwshomo.afip.gov.ar/wsmtxca/services/MTXCAService";
    public static String AFIP_LAST_BILL_ID = "https://fwshomo.afip.gov.ar/wsmtxca/services/MTXCAService";
    public static String AFIP_BILLIMG      = "https://fwshomo.afip.gov.ar/wsmtxca/services/MTXCAService";


    @Override
    public Person createPersonRequest(final String token,
                                      final String sign,
                                      final String cuitRepresentada,
                                      final String idPersona) {
        try {
            java.net.URL endPoint = new java.net.URL(AFIP_A5_SERVICE);
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(token, sign, cuitRepresentada, idPersona), endPoint);
            String result = printSOAPResponse(soapResponse);
            PersonPayload personPayload = convertToPersonPayload(result);
            soapConnection.close();

            return personPayload.getPerson();
        }catch (Exception e) {
            System.out.print("ERROR DE PARSEO ===========> " + e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LastBillIdResponse getLastBillId(final LoginTicketResponse ticketResponse,
                                            final LastBillIdResponse lastBillIdResponse) {
        SOAPMessage request = createGetLastBillId(ticketResponse, lastBillIdResponse);
        String requestAsAString = printSOAPResponse(request);
        SOAPMessage response = callService(AFIP_LAST_BILL_ID, request);
        String asAString = printSOAPResponse(response);
        LastBillIdResponse lastBillId = convertoToLastBillId(asAString);

        return lastBillId;

    }

    @Override
    public CAEAResponse createCAERequest(final LoginTicketResponse ticketResponse,
                                         final String CUIT) {
        SOAPMessage request = createGetCAE(ticketResponse, CUIT);
        String requestAsAString = printSOAPResponse(request);
        SOAPMessage response = callService(AFIP_CAE, request);
        String asAString = printSOAPResponse(response);
        CAEAResponse caeaResponse = convertToCAE(asAString);

        return caeaResponse;
    }

    @Override
    public BillResponse createBilling(final LoginTicketResponse ticketResponse,
                                      final BillRequest billRequest) {

        LastBillIdResponse lastBillIdRequest = new LastBillIdResponse(billRequest.getCuit(), billRequest.getBillType());
        LastBillIdResponse lastBillId = this.getLastBillId(ticketResponse, lastBillIdRequest);

        SOAPMessage request = createBill(ticketResponse, billRequest, lastBillId);
        String requestAsAString = printSOAPResponse(request);
        SOAPMessage response = callService(AFIP_BILLIMG, request);
        String asAString = printSOAPResponse(response);
        BillResponse billResponse = convertoToBillResponse(asAString);

        return billResponse;
    }

    private SOAPMessage callService(final String webService,
                                    final SOAPMessage request) {
        try {
            java.net.URL endPoint = new java.net.URL(webService);
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            SOAPMessage prepareCAE = request;
            SOAPMessage soapResponse = soapConnection.call(prepareCAE, endPoint);

            return soapResponse;

        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
