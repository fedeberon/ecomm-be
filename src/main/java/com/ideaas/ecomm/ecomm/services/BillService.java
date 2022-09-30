package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.converts.exceptions.Errors;
import com.ideaas.ecomm.ecomm.converts.exceptions.Fault;
import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.Person;
import com.ideaas.ecomm.ecomm.domain.AFIP.ResponsePerson;
import com.ideaas.ecomm.ecomm.domain.Bill;
import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.Item;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.enums.BillType;
import com.ideaas.ecomm.ecomm.enums.WalletTransactionType;
import com.ideaas.ecomm.ecomm.exception.AfipException;
import com.ideaas.ecomm.ecomm.exception.LoginTicketException;
import com.ideaas.ecomm.ecomm.payload.BillRequest;
import com.ideaas.ecomm.ecomm.payload.BillResponse;
import com.ideaas.ecomm.ecomm.payload.CAEAResponse;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;
import com.ideaas.ecomm.ecomm.payload.PersonPayload;
import com.ideaas.ecomm.ecomm.repository.BillDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IAfipService;
import com.ideaas.ecomm.ecomm.services.interfaces.IBillService;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import com.ideaas.ecomm.ecomm.services.interfaces.IPersonaService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import com.ideaas.ecomm.ecomm.services.interfaces.IWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertToCAE;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertToPersonPayload;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertoToBillResponse;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertoToLastBillId;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.hasFault;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.printSOAPResponse;
import static com.ideaas.ecomm.ecomm.converts.AfipExceptionConvert.convertToErrorAfip;
import static com.ideaas.ecomm.ecomm.converts.AfipValidationConverter.convertToValidationAfip;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createBillTyoeC;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createGetCAE;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createGetLastBillId;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createGetLastBillIdTypeC;
import static com.ideaas.ecomm.ecomm.services.AfipWSAAClient.createGetPersona;

@Service
public class BillService implements IBillService {

    private static final Logger logger = LoggerFactory.getLogger(BillService.class);

    // wsmtxca

    public static String AFIP_A5_SERVICE   = "https://aws.afip.gov.ar/sr-padron/webservices/personaServiceA5";
    /*
    public static String AFIP_CAE          = "https://fwshomo.afip.gov.ar/wsmtxca/services/MTXCAService";
    public static String AFIP_LAST_BILL_ID = "https://fwshomo.afip.gov.ar/wsmtxca/services/MTXCAService";
    public static String AFIP_BILLIMG      = "https://fwshomo.afip.gov.ar/wsmtxca/services/MTXCAService";
    */

    // https://wswhomo.afip.gov.ar/wsfev1/service.asmx
    //public static String AFIP_A5_SERVICE   = "https://awshomo.afip.gov.ar/sr-padron/webservices/personaServiceA5";
    public static String AFIP_CAE          = "https://fwshomo.afip.gov.ar/wsmtxca/services/MTXCAService";
    public static String AFIP_LAST_BILL_ID = "https://wswhomo.afip.gov.ar/wsfev1/service.asmx?op=FECompUltimoAutorizado";
    public static String AFIP_BILLING      = "https://wswhomo.afip.gov.ar/wsfev1/service.asmx?op=FECAESolicitar";
    public static String AFIP_BILLING_TYPE = "https://wswhomo.afip.gov.ar/wsfev1/service.asmx?op=FECompConsultar";

    private ICheckoutService checkoutService;
    private BillDao dao;
    private IUserService userService;
    private IWalletService walletService;
    private IProductService productService;
    private IAfipService afipService;
    private IPersonaService personService;

    @Autowired
    public BillService(final ICheckoutService checkoutService,
                       final BillDao dao,
                       final IUserService userService,
                       final IWalletService walletService,
                       final IProductService productService,
                       final IAfipService afipService,
                       final IPersonaService personService){
        this.checkoutService = checkoutService;
        this.dao = dao;
        this.userService = userService;
        this.walletService = walletService;
        this.productService = productService;
        this.afipService = afipService;
        this.personService = personService;
    }


    @Override
    public ResponsePerson createPersonRequest(final String token,
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
            ResponsePerson response = new ResponsePerson();
            String result = printSOAPResponse(soapResponse);
            Fault fault = hasFault(result);
            if (Objects.nonNull(fault)) {
                response.setFault(fault);
            } else {
                PersonPayload personPayload = convertToPersonPayload(result);
                response.setPersonPayload(personPayload);
            }
            soapConnection.close();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LastBillIdResponse getLastBillId(final LoginTicketResponse ticketResponse,
                                            final LastBillIdResponse lastBillIdResponse,
                                            final BillType billType) {
        String asAString = null;
        try {
            SOAPMessage request = null;

            switch (billType) {
                case A:
                case B:
                    request = createGetLastBillIdTypeC(ticketResponse, lastBillIdResponse);
                    //request = createGetLastBillId(ticketResponse, lastBillIdResponse);
                    break;
                case C:
                    request = createGetLastBillIdTypeC(ticketResponse, lastBillIdResponse);
                    break;
            }

            createGetLastBillId(ticketResponse, lastBillIdResponse);
            String requestAsAString = printSOAPResponse(request);
            logger.info("Request: " + requestAsAString);
            SOAPMessage response = callService(AFIP_LAST_BILL_ID, request);
            asAString = printSOAPResponse(response);
            logger.info("Response: " + asAString);
            LastBillIdResponse lastBillId = convertoToLastBillId(asAString);

            return lastBillId;
        } catch (Exception e) {
            Errors errors = convertToErrorAfip(asAString);
            logger.error("Error: " + errors.toString());
            throw new AfipException("There was a problem with AFIP services. Exception: " + errors);
        }
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
        try {
            final Checkout checkout = checkoutService.get(billRequest.getCheckoutId());
            prepareBillingItems(billRequest, checkout);
            final LastBillIdResponse lastBillIdRequest = new LastBillIdResponse("20285640661", billRequest.getBillType());
            final LastBillIdResponse lastBillId = this.getLastBillId(ticketResponse, lastBillIdRequest, billRequest.getBillType());
            final SOAPMessage request = createBillTyoeC(ticketResponse, billRequest, lastBillId);
            final String requestAsAString = printSOAPResponse(request);
            logger.info("Request: " + requestAsAString);

            final SOAPMessage response = callService(AFIP_BILLING, request);
            final String asAString = printSOAPResponse(response);
            logger.info("AFIP response: " + asAString);

            if (response.getSOAPBody().hasFault()) {
                final Fault fault = convertToValidationAfip(asAString);

                throw new AfipException("[AFIP ERROR]: Description: " + fault.getDetail());
            }

            BillResponse billResponse = convertoToBillResponse(asAString);
            billResponse.setNroComprobante(lastBillId.nextBillId().longValue());
            billResponse.setBillType(billRequest.getBillType());
            billResponse.setCreditCard(billRequest.getCreditCard());
            billResponse.setCoupon(billRequest.getCoupon());
            billResponse.setCUIT(billRequest.getCuit());

            return billResponse;

        } catch (LoginTicketException ex) {
            logger.error("[LoginTicketException]: " + ex.getMessage());
            throw ex;
        } catch (AfipException ex) {
            logger.error("[LoginTicketException]: " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("[AFIP ERROR]: " + ex.getMessage());
            throw new AfipException("There was a problem with AFIP services. Exception: " + ex);
        }
    }

    @Override
    public void getBillTypes(final LoginTicketResponse ticketResponse) {
        try {
            final SOAPMessage request = AfipWSAAClient.createGetTiposCbte(ticketResponse);
            final String requestAsAString = printSOAPResponse(request);
            logger.info("Request: " + requestAsAString);
            final SOAPMessage response = callService(AFIP_BILLING_TYPE, request);
            final String asAString = printSOAPResponse(response);
            logger.info("AFIP response: " + asAString);

            if(response.getSOAPBody().hasFault()) {
                final Fault fault = convertToValidationAfip(asAString);

                throw new AfipException("[AFIP ERROR]: Description: " + fault.getDetail());
            }

        } catch (LoginTicketException ex) {
            logger.error("[LoginTicketException]: " + ex.getMessage());
            throw ex;
        } catch (AfipException ex) {
            logger.error("[LoginTicketException]: " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("[AFIP ERROR]: " + ex.getMessage());
            throw new AfipException("There was a problem with AFIP services. Exception: " + ex);
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
    public Bill save(final BillResponse response, final Checkout checkout) {
        final User user = userService.get(checkout.getUsername());
        LoginTicketResponse ticketResponse = afipService.get("ws_sr_padron_a5");
        ResponsePerson personRequest = createPersonRequest(ticketResponse.getToken(), ticketResponse.getSign(), "20285640661", response.getCUIT());
        Person person = null;
        if (Objects.nonNull(personRequest) && personRequest.getFault() == null) {
             person = personService.save(personRequest.getPersonPayload().getPerson());
        }

        Bill bill = new Bill.BillBuilder()
                .withCAE(response.getCAE())
                .withBillType(response.getBillType().getCode())
                .withCuit(response.getDocNro())
                .withDueDateCAE(response.getCAEFchVto())
                .withNumber(response.getNroComprobante())
                .withPointNumber(response.getPuntoDeVenta())
                .withDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .withCheckout(checkout)
                .withUser(user)
                .withCoupon(response.getCoupon())
                .withCreditCard(response.getCreditCard())
                .withPerson(person)
                .build();
        
                walletService.productToCartInWallet(user, bill.getCheckout().getProducts(), WalletTransactionType.BUY);
                productService.discountAmountStock(bill.getCheckout().getProducts());


        return dao.save(bill);
    }



    @Override
    public List<Bill> findAll(){
        return dao.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Bill get(Long id) {
        return dao.findById(id).get();
    }

    @Override
    public List<Bill> findAllByUser(final User user){
        return dao.findAllByUser(user, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public List<Bill> search(final BillResponse response, final String start, final String end) {
        return dao.findAllByCreditCardAndAndDateBetween(response.getCreditCard(), start, end);
    }

}
