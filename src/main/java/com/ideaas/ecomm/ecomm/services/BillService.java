package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.converts.exceptions.Errors;
import com.ideaas.ecomm.ecomm.converts.exceptions.Fault;
import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.Person;
import com.ideaas.ecomm.ecomm.domain.Bill;
import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.Item;
import com.ideaas.ecomm.ecomm.domain.Product;
import com.ideaas.ecomm.ecomm.domain.ProductToCart;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.domain.Wallet;
import com.ideaas.ecomm.ecomm.exception.AfipException;
import com.ideaas.ecomm.ecomm.exception.LoginTicketException;
import com.ideaas.ecomm.ecomm.payload.BillRequest;
import com.ideaas.ecomm.ecomm.payload.BillResponse;
import com.ideaas.ecomm.ecomm.payload.CAEAResponse;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;
import com.ideaas.ecomm.ecomm.payload.PersonPayload;
import com.ideaas.ecomm.ecomm.repository.BillDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IBillService;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import com.ideaas.ecomm.ecomm.services.interfaces.IProductService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import com.ideaas.ecomm.ecomm.services.interfaces.IWalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertToCAE;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertToPersonPayload;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertoToBillResponse;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.convertoToLastBillId;
import static com.ideaas.ecomm.ecomm.converts.AfipConvert.printSOAPResponse;
import static com.ideaas.ecomm.ecomm.converts.AfipExceptionConvert.convertToErrorAfip;
import static com.ideaas.ecomm.ecomm.converts.AfipValidationConverter.convertToValidationAfip;
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
    private IUserService userService;
    private IWalletService walletService;
    private IProductService productService;



    @Autowired
    public BillService(final ICheckoutService checkoutService,
                       final BillDao dao,
                       final IUserService userService,
                       final IWalletService walletService,
                       final IProductService productService ){
        this.checkoutService = checkoutService;
        this.dao = dao;
        this.userService = userService;
        this.walletService = walletService;
        this.productService = productService;
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
        String asAString = null;
        try {
            SOAPMessage request = createGetLastBillId(ticketResponse, lastBillIdResponse);
            String requestAsAString = printSOAPResponse(request);
            SOAPMessage response = callService(AFIP_LAST_BILL_ID, request);
            asAString = printSOAPResponse(response);
            LastBillIdResponse lastBillId = convertoToLastBillId(asAString);

            return lastBillId;
        } catch (Exception e) {
            Errors errors = convertToErrorAfip(asAString);

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
            final LastBillIdResponse lastBillId = this.getLastBillId(ticketResponse, lastBillIdRequest);

            final SOAPMessage request = createBill(ticketResponse, billRequest, lastBillId);
            final String requestAsAString = printSOAPResponse(request);
            final SOAPMessage response = callService(AFIP_BILLIMG, request);
            final String asAString = printSOAPResponse(response);

            if(response.getSOAPBody().hasFault()) {
                final Fault fault = convertToValidationAfip(asAString);

                throw new AfipException("[AFIP ERROR]: Description: " + fault.getDetail());
            }

            final BillResponse billResponse = convertoToBillResponse(asAString);

            return billResponse;

        } catch (LoginTicketException ex) {
            throw ex;
        } catch (AfipException ex) {
            throw ex;
        } catch (Exception ex) {
            //Errors errors = convertToErrorAfip(asAString);
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
    public Bill save(BillResponse response) {
        final User user = userService.getCurrent();
        Bill bill = new Bill.BillBuilder()
                .withBillType(response.getVoucher().getBillType())
                .withCAE(response.getVoucher().getCAE())
                .withCuit(response.getVoucher().getCuit())
                .withDate(response.getVoucher().getDate())
                .withDueDateCAE(response.getVoucher().getDueDateCAE())
                .withNumber(response.getVoucher().getNumber())
                .withPointNumber(response.getVoucher().getPointNumber())
                .withCheckout(response.getCheckout())
                .withUser(user)
                .build();
        
                productToCartInWallet(user, bill.getCheckout().getProducts());  
                discountAmountStock(bill.getCheckout().getProducts());


        return dao.save(bill);
    }


    private void productToCartInWallet(final User user, final List<ProductToCart> productToCarts){
        List<Wallet> wallets = new ArrayList<>();
        productToCarts.forEach(productToCart -> {
            Product product = productToCart.getProduct();
            Long points = Objects.isNull(product.getPoints()) || product.getPoints() == 0
                            ? Math.round(product.getPrice() * 5 / 100)
                            : product.getPoints();
            Wallet oneWallet = new Wallet(product,
                                          user,
                                          productToCart.getQuantity(),
                                          points * productToCart.getQuantity());
            wallets.add(oneWallet);
        });

        walletService.saveAll(wallets);
    }



    private void discountAmountStock(final List<ProductToCart> productToCarts) {
        productToCarts.forEach(productToCart -> {
            Product product = productToCart.getProduct();
            Long stock =  product.getStock() - productToCart.getQuantity();
            product.setStock(stock);
            productService.save(product);
        });
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

}
