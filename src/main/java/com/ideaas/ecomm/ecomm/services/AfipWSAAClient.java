package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.domain.Item;
import com.ideaas.ecomm.ecomm.enums.BillType;
import com.ideaas.ecomm.ecomm.enums.IVAConditionType;
import com.ideaas.ecomm.ecomm.enums.IdCartType;
import com.ideaas.ecomm.ecomm.payload.BillRequest;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.Base64;
import org.apache.axis.encoding.XMLType;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.xml.rpc.ParameterMode;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


@SuppressWarnings("all")
@Component
public class AfipWSAAClient {

    private static final Logger logger = LoggerFactory.getLogger(AfipWSAAClient.class);

    static DecimalFormat df = new DecimalFormat("###.##");


    //https://www.afip.gob.ar/fe/ayuda/documentos/Manual-desarrollador-V.0.16.pdf

    static String invokeWSAA(byte [] LoginTicketRequest_xml_cms, String endpoint){
        String LoginTicketResponse = null;
        try {

            Service service = new Service();
            Call call = (Call) service.createCall();

            //
            // Prepare the call for the Web service
            //
            call.setTargetEndpointAddress( new java.net.URL(endpoint) );
            call.setOperationName("loginCms");
            call.addParameter( "request", XMLType.XSD_STRING, ParameterMode.IN );
            call.setReturnType( XMLType.XSD_STRING );

            //
            // Make the actual call and assign the answer to a String
            //
            LoginTicketResponse = (String) call.invoke(new Object[] { Base64.encode (LoginTicketRequest_xml_cms) } );

            logger.info("LoginTicketResponse {}" , LoginTicketResponse);

        } catch (Exception e) {
            logger.info("Excepcion {}" , LoginTicketResponse);
            e.printStackTrace();
        }
        return (LoginTicketResponse);
    }

    public static byte [] create_cms(String p12file, String p12pass, String signer, String dstDN, String service) {
        PrivateKey pKey = null;
        X509Certificate pCertificate = null;
        byte [] asn1_cms = null;
        CertStore cstore = null;
        String LoginTicketRequest_xml;
        String SignerDN = null;

        //
        // Manage Keys & Certificates
        //
        try {
            // Create a keystore using keys from the pkcs#12 p12file
            KeyStore ks = KeyStore.getInstance("pkcs12");
            FileInputStream p12stream = new FileInputStream( p12file ) ;
            logger.info("p12pass {}" , p12pass);
            ks.load(p12stream, p12pass.toCharArray());
            logger.info("ks size {}" , ks.size());
            p12stream.close();

            // Get Certificate & Private key from KeyStore
            pKey = (PrivateKey) ks.getKey(signer, p12pass.toCharArray());
            logger.info("pKey {}" , pKey);
            pCertificate = (X509Certificate)ks.getCertificate(signer);
            logger.info("pCertificate {}" , pCertificate);

            SignerDN = pCertificate.getSubjectDN().toString();
            logger.info("SignerDN {}" , SignerDN);

            // Create a list of Certificates to include in the final CMS
            ArrayList<X509Certificate> certList = new ArrayList<X509Certificate>();
            certList.add(pCertificate);

            if (Security.getProvider("BC") == null) {
                Security.addProvider(new BouncyCastleProvider());
            }

            cstore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");
        }
        catch (IOException | NoSuchAlgorithmException | CertificateException ex) {
            logger.info("Excepcion {}" , ex);
        }
        catch (Exception e) {
            logger.info("Exception {}" , e);
            e.printStackTrace();
        }

        //
        // Create XML Message
        //
        LoginTicketRequest_xml = create_LoginTicketRequest(SignerDN, dstDN, service);

        //
        // Create CMS Message
        //
        try {
            // Create a new empty CMS Message
            CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

            // Add a Signer to the Message
            gen.addSigner(pKey, pCertificate, CMSSignedDataGenerator.DIGEST_SHA1);

            // Add the Certificate to the Message
            gen.addCertificatesAndCRLs(cstore);

            // Add the data (XML) to the Message
            CMSProcessable data = new CMSProcessableByteArray(LoginTicketRequest_xml.getBytes());

            // Add a Sign of the Data to the Message
            CMSSignedData signed = gen.generate(data, true, "BC");

            //
            asn1_cms = signed.getEncoded();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return (asn1_cms);
    }

    public static String create_LoginTicketRequest (String SignerDN, String dstDN, String service) {
        String LoginTicketRequest_xml;
        Date GenTime = new Date();
        GregorianCalendar gentime = new GregorianCalendar();
        GregorianCalendar exptime = new GregorianCalendar();
        String UniqueId = new Long(GenTime.getTime() / 1000).toString();
        exptime.setTime(new Date(GenTime.getTime() +  1000));
        LocalDateTime start = LocalDateTime.now();

        LoginTicketRequest_xml = "<?xml version=\"1.0\" encoding=\"UTF\u00AD8\"?>"
                                +"<loginTicketRequest version=\"1.0\">"
                                +"<header>"
                                +"<source>" + SignerDN + "</source>"
                                +"<destination>" + dstDN + "</destination>"
                                +"<uniqueId>" + UniqueId + "</uniqueId>"
                                +"<generationTime>" + start + "</generationTime>"
                                +"<expirationTime>" + start.plusMinutes(1) + "</expirationTime>"
                                +"</header>"
                                +"<service>" + service + "</service>"
                                +"</loginTicketRequest>";

        System.out.println("TRA: " + LoginTicketRequest_xml);

        return (LoginTicketRequest_xml);
    }


    public static SOAPMessage createGetLastBillId(final LoginTicketResponse ticketResponse,
                                                  final LastBillIdResponse lastBillIdResponse) {
        try {
            MessageFactory messageFactory  = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            javax.xml.soap.SOAPBody soapBody = envelope.getBody();
            envelope.addNamespaceDeclaration("ser", "http://impl.service.wsmtxca.afip.gov.ar/service/");
            SOAPElement consultarUltimoComprobanteAutorizadoElem = soapBody.addChildElement("consultarUltimoComprobanteAutorizadoRequest", "ser");
            SOAPElement authElem = consultarUltimoComprobanteAutorizadoElem.addChildElement("authRequest");
            SOAPElement tokenElement = authElem.addChildElement("token");
            tokenElement.addTextNode(ticketResponse.getToken());
            SOAPElement signElement = authElem.addChildElement("sign");
            signElement.addTextNode(ticketResponse.getSign());
            SOAPElement cuitElement = authElem.addChildElement("cuitRepresentada");
            cuitElement.addTextNode(lastBillIdResponse.getCuit());

            SOAPElement consultaUltimoComprobanteAutorizadoRequest = consultarUltimoComprobanteAutorizadoElem.addChildElement("consultaUltimoComprobanteAutorizadoRequest");
            SOAPElement codigoTipoComprobanteElement = consultaUltimoComprobanteAutorizadoRequest.addChildElement("codigoTipoComprobante");
            codigoTipoComprobanteElement.setTextContent(lastBillIdResponse.getBillType().getCode());
            SOAPElement numeroPuntoVentaElement = consultaUltimoComprobanteAutorizadoRequest.addChildElement("numeroPuntoVenta");
            numeroPuntoVentaElement.setTextContent("1");

            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "http://impl.service.wsmtxca.afip.gov.ar/service/consultarUltimoComprobanteAutorizado");
            soapMessage.saveChanges();

            return soapMessage;

        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static SOAPMessage createGetCAE(final LoginTicketResponse ticketResponse,
                                           final String CUIT) {
        try {
            MessageFactory messageFactory  = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            javax.xml.soap.SOAPBody soapBody = envelope.getBody();
            envelope.addNamespaceDeclaration("ser", "http://impl.service.wsmtxca.afip.gov.ar/service/");
            SOAPElement consultarUltimoComprobanteAutorizadoElem = soapBody.addChildElement("solicitarCAEARequest", "ser");
            SOAPElement authElem = consultarUltimoComprobanteAutorizadoElem.addChildElement("authRequest");
            SOAPElement tokenElement = authElem.addChildElement("token");
            tokenElement.addTextNode(ticketResponse.getToken());
            SOAPElement signElement = authElem.addChildElement("sign");
            signElement.addTextNode(ticketResponse.getSign());
            SOAPElement cuitElement = authElem.addChildElement("cuitRepresentada");
            cuitElement.addTextNode(CUIT);

            SOAPElement solicitudCAEA = consultarUltimoComprobanteAutorizadoElem.addChildElement("solicitudCAEA");
            SOAPElement periodoElement = solicitudCAEA.addChildElement("periodo");
            periodoElement.setTextContent("202110");
            SOAPElement ordenElement = solicitudCAEA.addChildElement("orden");
            ordenElement.setTextContent("2");

            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "http://impl.service.wsmtxca.afip.gov.ar/service/solicitarCAEA");
            soapMessage.saveChanges();

            return soapMessage;

        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static SOAPMessage createBill(final LoginTicketResponse ticketResponse,
                                         final BillRequest billRequest,
                                         final LastBillIdResponse lastBillIdResponse) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            MessageFactory messageFactory  = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            javax.xml.soap.SOAPBody soapBody = envelope.getBody();
            envelope.addNamespaceDeclaration("ser", "http://impl.service.wsmtxca.afip.gov.ar/service/");
            SOAPElement autorizarComprobanteRequestElement = soapBody.addChildElement("autorizarComprobanteRequest", "ser");
            SOAPElement authElem = autorizarComprobanteRequestElement.addChildElement("authRequest");
            SOAPElement tokenElement = authElem.addChildElement("token");
            tokenElement.addTextNode(ticketResponse.getToken());
            SOAPElement signElement = authElem.addChildElement("sign");
            signElement.addTextNode(ticketResponse.getSign());
            SOAPElement cuitElement = authElem.addChildElement("cuitRepresentada");
            cuitElement.addTextNode(billRequest.getCuit());

            SOAPElement comprobanteCAERequestElement = autorizarComprobanteRequestElement.addChildElement("comprobanteCAERequest");
            SOAPElement codigoTipoComprobanteElement = comprobanteCAERequestElement.addChildElement("codigoTipoComprobante");
            codigoTipoComprobanteElement.addTextNode(billRequest.getBillType().getCode());
            SOAPElement numeroPuntoVentaElement = comprobanteCAERequestElement.addChildElement("numeroPuntoVenta");
            numeroPuntoVentaElement.addTextNode(String.valueOf(billRequest.getPuntoDeVenta()));
            SOAPElement numeroComprobanteElement = comprobanteCAERequestElement.addChildElement("numeroComprobante");
            numeroComprobanteElement.addTextNode(String.valueOf(lastBillIdResponse.nextBillId()));
            SOAPElement fechaEmisionElement = comprobanteCAERequestElement.addChildElement("fechaEmision");
            fechaEmisionElement.addTextNode(formatter.format(billRequest.getDate()));

            SOAPElement codigoTipoDocumentoElement = comprobanteCAERequestElement.addChildElement("codigoTipoDocumento");
            codigoTipoDocumentoElement.addTextNode(getIdCardType(billRequest));
            SOAPElement numeroDocumentoElement = comprobanteCAERequestElement.addChildElement("numeroDocumento");
            numeroDocumentoElement.addTextNode(billRequest.getCardId());

            SOAPElement importeGravadoElement = comprobanteCAERequestElement.addChildElement("importeGravado");
            importeGravadoElement.addTextNode(String.valueOf(billRequest.getTotalAmount()));
            SOAPElement importeNoGravadoElement = comprobanteCAERequestElement.addChildElement("importeNoGravado");
            importeNoGravadoElement.addTextNode("0.00");
            SOAPElement importeExentoElement = comprobanteCAERequestElement.addChildElement("importeExento");
            importeExentoElement.addTextNode("0.00");
            SOAPElement importeSubtotalElement = comprobanteCAERequestElement.addChildElement("importeSubtotal");
            importeSubtotalElement.addTextNode(String.valueOf(billRequest.getSubtotal()));

            SOAPElement importeTotalElement = comprobanteCAERequestElement.addChildElement("importeTotal");
            importeTotalElement.addTextNode(df.format(calculateTotal(billRequest)));

            SOAPElement codigoMonedaElement = comprobanteCAERequestElement.addChildElement("codigoMoneda");
            codigoMonedaElement.addTextNode("PES");
            SOAPElement cotizacionMonedaElement = comprobanteCAERequestElement.addChildElement("cotizacionMoneda");
            cotizacionMonedaElement.addTextNode("1");
            SOAPElement observacionesElement = comprobanteCAERequestElement.addChildElement("observaciones");
            observacionesElement.addTextNode(billRequest.getComments());
            SOAPElement codigoConceptoElement = comprobanteCAERequestElement.addChildElement("codigoConcepto");
            codigoConceptoElement.addTextNode("1");
            SOAPElement arrayItemsElement = comprobanteCAERequestElement.addChildElement("arrayItems");

            billRequest.getItems().forEach(item -> {
                try {
                    SOAPElement itemElement = arrayItemsElement.addChildElement("item");
                    SOAPElement unidadesMtxElement = itemElement.addChildElement("unidadesMtx");
                    unidadesMtxElement.addTextNode(String.valueOf(item.getQuantity()));
                    SOAPElement codigoMtxElement = itemElement.addChildElement("codigoMtx");
                    codigoMtxElement.addTextNode(item.getCode());
                    SOAPElement codigoElement = itemElement.addChildElement("codigo");
                    codigoElement.addTextNode(item.getCode());
                    SOAPElement descripcionElement = itemElement.addChildElement("descripcion");
                    descripcionElement.addTextNode(item.getDescription());
                    SOAPElement codigoCantidadElement = itemElement.addChildElement("cantidad");
                    codigoCantidadElement.addTextNode(String.valueOf(item.getQuantity()));
                    SOAPElement codigoUnidadMedidaElement = itemElement.addChildElement("codigoUnidadMedida");
                    codigoUnidadMedidaElement.addTextNode("1");
                    SOAPElement precioUnitarioElement = itemElement.addChildElement("precioUnitario");
                    precioUnitarioElement.addTextNode(String.valueOf(item.getPrice()));
                    SOAPElement codigoCondicionIVAElement = itemElement.addChildElement("codigoCondicionIVA");
                    if(billRequest.getBillType().equals(BillType.A)) {
                        SOAPElement importeIVAAElement = itemElement.addChildElement("importeIVA");
                        importeIVAAElement.addTextNode(String.valueOf(getIva(billRequest, item)));
                    }
                    codigoCondicionIVAElement.addTextNode(getContitionType(billRequest));
                    SOAPElement importeItemElement = itemElement.addChildElement("importeItem");
                    importeItemElement.addTextNode(df.format(getPriceItem(billRequest, item)));
                    arrayItemsElement.addChildElement(itemElement);
                } catch (SOAPException e) {
                    e.printStackTrace();
                }
            });

            if(billRequest.getBillType().equals(BillType.A)) {

                SOAPElement arraySubtotalesIVAElement = comprobanteCAERequestElement.addChildElement("arraySubtotalesIVA");
                SOAPElement subtotalIVAElement = arraySubtotalesIVAElement.addChildElement("subtotalIVA");

                SOAPElement codigoSubtotalIVAElement = subtotalIVAElement.addChildElement("codigo");
                codigoSubtotalIVAElement.addTextNode(getContitionType(billRequest));
                subtotalIVAElement.addChildElement(codigoSubtotalIVAElement);

                SOAPElement importeSubtotalIVAElement = subtotalIVAElement.addChildElement("importe");
                importeSubtotalIVAElement.addTextNode(df.format(getCalculateAllIvaValues(billRequest)));
                subtotalIVAElement.addChildElement(importeSubtotalIVAElement);
                arraySubtotalesIVAElement.addChildElement(subtotalIVAElement);
            }

            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "http://impl.service.wsmtxca.afip.gov.ar/service/autorizarComprobante");
            soapMessage.saveChanges();

            return soapMessage;

        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Double getCalculateAllIvaValues(final BillRequest billRequest) {
        return billRequest.getItems().stream().mapToDouble(i -> getIva(billRequest, i)).sum();
    }

    private static Double calculateTotal(final BillRequest billRequest) {
        Double result = getCalculateAllIvaValues(billRequest);

        return billRequest.getTotalAmount() + result;
    }

    private static Double getIva(final BillRequest billRequest,
                                 final Item item){
        switch (billRequest.getBillType()) {
            case A:
                return item.getPrice() * item.getQuantity()  * 21 / 100;
            case B:
                return 0.00;
        }
        throw new IllegalStateException("There was not posible find a 'IVA' condition type to return a value.");
    }

    private static Double getPriceItem(final BillRequest billRequest,
                                       final Item item) {
        switch (billRequest.getBillType()) {
            case A:
                return item.getPrice() * item.getQuantity() + getIva(billRequest, item);
            case B:
                return item.getPrice() * item.getQuantity();
        }
        throw new IllegalStateException("There was not posible calculate price item.");
    }

    private static String getContitionType(final BillRequest billRequest) {
        switch (billRequest.getBillType()) {
            case A:
                return IVAConditionType.VEINTIUNO_PORCIENTO.getCode();
            case B:
                return IVAConditionType.O_POCIENTO.getCode();
        }
        throw new IllegalStateException("There was not posible find a 'IVA' condition type");
    }

    private static String getIdCardType(BillRequest billRequest) {
        switch (billRequest.getBillType()) {
            case A:
                return IdCartType.CUIT.getCode();
            case B:
                return IdCartType.DNI.getCode();
        }
        throw new IllegalStateException("There was not posible find a 'TIPO DE DNI' type");
    }

    public static SOAPMessage createGetPersona(final String token,
                                               final String sign,
                                               final String cuitRepresentada,
                                               final String idPersona) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String serverURI = "http://a5.soap.ws.server.puc.sr/";
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("a5", serverURI);
        javax.xml.soap.SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("getPersona", "a5");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("token");
        soapBodyElem1.addTextNode(token);
        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("sign");
        soapBodyElem2.addTextNode(sign);
        SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("cuitRepresentada");
        soapBodyElem3.addTextNode(cuitRepresentada);
        SOAPElement soapBodyElem4 = soapBodyElem.addChildElement("idPersona");
        soapBodyElem4.addTextNode(idPersona);
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI + "getPersona");
        soapMessage.saveChanges();

        return soapMessage;
    }




}
