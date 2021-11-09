package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.Person;
import com.ideaas.ecomm.ecomm.domain.Bill;
import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.Item;
import com.ideaas.ecomm.ecomm.exception.LoginTicketException;
import com.ideaas.ecomm.ecomm.payload.BillRequest;
import com.ideaas.ecomm.ecomm.payload.BillResponse;
import com.ideaas.ecomm.ecomm.payload.CAEAResponse;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;
import com.ideaas.ecomm.ecomm.payload.PersonPayload;
import com.ideaas.ecomm.ecomm.repository.BillDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IBillService;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;
import java.util.List;

import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertToCAE;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertToPersonPayload;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertoToBillResponse;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertoToLastBillId;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.printSOAPResponse;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createBill;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createGetCAE;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createGetLastBillId;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createGetPersona;

@Service
public class BillService implements IBillService {

    public static String AFIP_A5_SERVICE   = "https://aws.afip.gov.ar/sr-padron/webservices/personaServiceA5";
    public static String AFIP_CAE          = "https://fwshomo.afip.gov.ar/wsmtxca/services/MTXCAService";
    public static String AFIP_LAST_BILL_ID = "https://fwshomo.afip.gov.ar/wsmtxca/services/MTXCAService";
    public static String AFIP_BILLIMG      = "https://fwshomo.afip.gov.ar/wsmtxca/services/MTXCAService";

    private ICheckoutService checkoutService;

    private BillDao dao;

    @Autowired
    public BillService(final ICheckoutService checkoutService,
                       final BillDao dao) {
        this.checkoutService = checkoutService;
        this.dao = dao;
    }


    @Override
    public Person createPersonRequest(final String token,
                                      final String sign,
                                      final String cuitRepresentada,
                                      final String idPersona) {
        try {
            java.net.URL endPoint = new java.net.URL(AFIP_A5_SERVICE);
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            SOAPMessage request = createGetPersona(token, sign, cuitRepresentada, idPersona);
            String asAString = printSOAPResponse(request);
            SOAPMessage soapResponse = soapConnection.call(request, endPoint);
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
        Checkout checkout = checkoutService.get(billRequest.getCheckoutId());
        prepareBillingItems(billRequest, checkout);
        LastBillIdResponse lastBillIdRequest = new LastBillIdResponse(billRequest.getCuit(), billRequest.getBillType());
        LastBillIdResponse lastBillId = this.getLastBillId(ticketResponse, lastBillIdRequest);

        SOAPMessage request = createBill(ticketResponse, billRequest, lastBillId);
        String requestAsAString = printSOAPResponse(request);
        SOAPMessage response = callService(AFIP_BILLIMG, request);
        String asAString = printSOAPResponse(response);
        try {
            BillResponse billResponse = convertoToBillResponse(asAString);

            return billResponse;
        } catch (LoginTicketException ex) {
          throw ex;
        }

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

    private static BillRequest prepareBillingItems(final BillRequest billRequest, final Checkout checkout) {
        List<Item> items = new ArrayList<>(checkout.getProducts().size());
        checkout.getProducts().forEach(productToCart -> {
            Item item = Item.builder()
                    .id(productToCart.getProduct().getId())
                    .code(productToCart.getProduct().getId().toString())
                    .description(productToCart.getProduct().getName())
                    .quantity(productToCart.getQuantity())
                    .price(productToCart.getProduct().getPrice())
                    .build();
            items.add(item);
        });
        billRequest.setItems(items);

        return billRequest;
    }

    @Override
    public Bill save(BillResponse response) {
        Bill bill = new Bill.BillBuilder()
                .withBillType(response.getVoucher().getBillType())
                .withCAE(response.getVoucher().getCAE())
                .withCuit(response.getVoucher().getCuit())
                .withDate(response.getVoucher().getDate())
                .withDueDateCAE(response.getVoucher().getDueDateCAE())
                .withNumber(response.getVoucher().getNumber())
                .withPointNumber(response.getVoucher().getPointNumber())
                .withCheckout(response.getCheckout())
                .build();

        return dao.save(bill);
    }


    @Override
    public List<Bill> findAll(){
        return dao.findAll();
    }

}
