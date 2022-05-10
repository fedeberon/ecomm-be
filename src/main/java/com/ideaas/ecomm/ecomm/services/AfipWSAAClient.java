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
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertStore;
import java.security.cert.CertificateException;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.time.LocalDate;
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

    static String invokeWSAA(byte[] LoginTicketRequest_xml_cms, String endpoint) {
        String LoginTicketResponse = null;
        try {

            Service service = new Service();
            Call call = (Call) service.createCall();

            //
            // Prepare the call for the Web service
            //
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            call.setOperationName("loginCms");
            call.addParameter("request", XMLType.XSD_STRING, ParameterMode.IN);
            call.setReturnType(XMLType.XSD_STRING);

            //
            // Make the actual call and assign the answer to a String
            //
            LoginTicketResponse = (String) call.invoke(new Object[]{Base64.encode(LoginTicketRequest_xml_cms)});

            logger.info("LoginTicketResponse {}", LoginTicketResponse);

        } catch (Exception e) {
            logger.info("Excepcion {}", LoginTicketResponse);
            e.printStackTrace();
        }
        return (LoginTicketResponse);
    }

    public static byte[] create_cms(String p12file, String p12pass, String signer, String dstDN, String service) {
        PrivateKey pKey = null;
        X509Certificate pCertificate = null;
        byte[] asn1_cms = null;
        CertStore cstore = null;
        String LoginTicketRequest_xml;
        String SignerDN = null;

        //
        // Manage Keys & Certificates
        //

        // Create a keystore using keys from the pkcs#12 p12file
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("pkcs12");
        } catch (KeyStoreException ex) {
            ex.printStackTrace();
            logger.info("KeyStoreException {}", p12pass);
        }

        FileInputStream p12stream = null;
        try {
            p12stream = new FileInputStream(p12file);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            logger.info("FileNotFoundException {}", ex);
        }

        try {
            ks.load(p12stream, p12pass.toCharArray());
        } catch (IOException | NoSuchAlgorithmException | CertificateException ex) {
            logger.info("IOException 1 {}", ex);
        }
        try {
            p12stream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.info("IOException 2 {}", ex);
        }

        // Get Certificate & Private key from KeyStore
        try {
            pKey = (PrivateKey) ks.getKey(signer, p12pass.toCharArray());
        } catch (KeyStoreException ex) {
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (UnrecoverableKeyException ex) {
            ex.printStackTrace();
        }
        logger.info("pKey {}", pKey);
        try {
            pCertificate = (X509Certificate) ks.getCertificate(signer);
        } catch (KeyStoreException ex) {
            ex.printStackTrace();
        }
        logger.info("pCertificate {}", pCertificate);

        SignerDN = pCertificate.getSubjectDN().toString();
        logger.info("SignerDN {}", SignerDN);

        // Create a list of Certificates to include in the final CMS
        ArrayList<X509Certificate> certList = new ArrayList<X509Certificate>();
        certList.add(pCertificate);

        if (Security.getProvider("BC") == null) {
            Security.addProvider(new BouncyCastleProvider());
        }

        try {
            cstore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");
        } catch (InvalidAlgorithmParameterException ex) {
            ex.printStackTrace();
            logger.info("InvalidAlgorithmParameterException {}", ex);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            logger.info("NoSuchAlgorithmException {}", ex);
        } catch (NoSuchProviderException ex) {
            ex.printStackTrace();
            logger.info("NoSuchProviderException {}", ex);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (asn1_cms);
    }

    public static String create_LoginTicketRequest(String SignerDN, String dstDN, String service) {
        String LoginTicketRequest_xml;
        Date GenTime = new Date();
        GregorianCalendar gentime = new GregorianCalendar();
        GregorianCalendar exptime = new GregorianCalendar();
        String UniqueId = new Long(GenTime.getTime() / 1000).toString();
        exptime.setTime(new Date(GenTime.getTime() + 1000));
        LocalDateTime start = LocalDateTime.now();

        LoginTicketRequest_xml = "<?xml version=\"1.0\" encoding=\"UTF\u00AD8\"?>"
                + "<loginTicketRequest version=\"1.0\">"
                + "<header>"
                + "<source>" + SignerDN + "</source>"
                + "<destination>" + dstDN + "</destination>"
                + "<uniqueId>" + UniqueId + "</uniqueId>"
                + "<generationTime>" + start + "</generationTime>"
                + "<expirationTime>" + start.plusMinutes(1) + "</expirationTime>"
                + "</header>"
                + "<service>" + service + "</service>"
                + "</loginTicketRequest>";

        System.out.println("TRA: " + LoginTicketRequest_xml);

        return (LoginTicketRequest_xml);
    }


    public static SOAPMessage createGetLastBillId(final LoginTicketResponse ticketResponse,
                                                  final LastBillIdResponse lastBillIdResponse) {
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
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


    public static SOAPMessage createGetLastBillIdTypeC(final LoginTicketResponse ticketResponse,
                                                       final LastBillIdResponse lastBillIdResponse) {
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            javax.xml.soap.SOAPBody soapBody = envelope.getBody();
            envelope.addNamespaceDeclaration("ar", "http://ar.gov.afip.dif.FEV1/");
            SOAPElement fECompUltimoAutorizado = soapBody.addChildElement("FECompUltimoAutorizado", "ar");

            SOAPElement authElement = fECompUltimoAutorizado.addChildElement("Auth", "ar");
            SOAPElement tokenElement = authElement.addChildElement("Token", "ar");
            tokenElement.addTextNode(ticketResponse.getToken());
            SOAPElement signElement2 = authElement.addChildElement("Sign", "ar");
            signElement2.addTextNode(ticketResponse.getSign());
            SOAPElement cuitElement = authElement.addChildElement("Cuit", "ar");
            cuitElement.addTextNode(lastBillIdResponse.getCuit());

            SOAPElement ptoVta = fECompUltimoAutorizado.addChildElement("PtoVta", "ar");
            ptoVta.addTextNode("1");
            SOAPElement cbteTipo = fECompUltimoAutorizado.addChildElement("CbteTipo", "ar");
            cbteTipo.addTextNode(lastBillIdResponse.getBillType().getCode());

            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", "http://ar.gov.afip.dif.FEV1/FECompUltimoAutorizado");
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
            MessageFactory messageFactory = MessageFactory.newInstance();
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
            MessageFactory messageFactory = MessageFactory.newInstance();
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
                    if (billRequest.getBillType().equals(BillType.A)) {
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

            if (billRequest.getBillType().equals(BillType.A)) {

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
            logger.error("Error al crear el mensaje SOAP para el CAE", e);
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
                                 final Item item) {
        switch (billRequest.getBillType()) {
            case A:
                return item.getPrice() * item.getQuantity() * 21 / 100;
            case B:
                return 0.00;
            case C:
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

    /**
     * Invoke prepare the object to send to the webservice of a person.
     * @param token
     * @param sign
     * @param cuitRepresentada of the user authorized.
     * @param idPersona of the person.
     * @return the {@link SOAPMessage} to send to the webservice.
     * @throws Exception
     */
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


    public static SOAPMessage createBillTyoeC(final LoginTicketResponse ticketResponse,
                                              final BillRequest billRequest,
                                              final LastBillIdResponse lastBillIdResponse) {
        DateTimeFormatter formatterBill = DateTimeFormatter.ofPattern("YYYYMMdd");

        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            javax.xml.soap.SOAPBody soapBody = envelope.getBody();
            envelope.addNamespaceDeclaration("ar", "http://ar.gov.afip.dif.FEV1/");
            SOAPElement fECAESolicitar = soapBody.addChildElement("FECAESolicitar", "ar");

            SOAPElement auth = fECAESolicitar.addChildElement("Auth", "ar");
            SOAPElement tokenElement = auth.addChildElement("Token", "ar");
            tokenElement.addTextNode(ticketResponse.getToken());
            SOAPElement signElement = auth.addChildElement("Sign", "ar");
            signElement.addTextNode(ticketResponse.getSign());
            SOAPElement cuitElement = auth.addChildElement("Cuit", "ar");
            cuitElement.addTextNode("20285640661");

            SOAPElement feCAEReq = fECAESolicitar.addChildElement("FeCAEReq", "ar");

            SOAPElement feCabReq = feCAEReq.addChildElement("FeCabReq", "ar");
            SOAPElement cantReg = feCabReq.addChildElement("CantReg", "ar");
            cantReg.addTextNode("1");
            SOAPElement ptoVta = feCabReq.addChildElement("PtoVta", "ar");
            ptoVta.addTextNode(String.valueOf(billRequest.getPuntoDeVenta()));
            SOAPElement cbteTipo = feCabReq.addChildElement("CbteTipo", "ar");
            cbteTipo.addTextNode(String.valueOf(billRequest.getBillType().getCode()));

            // Detalle
            SOAPElement feDetReq = feCAEReq.addChildElement("FeDetReq", "ar");
            SOAPElement feDetReqArray = feDetReq.addChildElement("FECAEDetRequest", "ar");
            SOAPElement concepto = feDetReqArray.addChildElement("Concepto", "ar");
            concepto.addTextNode("1");
            SOAPElement docTipo = feDetReqArray.addChildElement("DocTipo", "ar");

            if (billRequest.getBillType().equals(BillType.B) || billRequest.getBillType().equals(BillType.A)) {

                if (billRequest.getBillType().equals(BillType.B) && billRequest.getTotalAmount() > 10000) {
                    docTipo.addTextNode("99");
                    SOAPElement docNro = feDetReqArray.addChildElement("DocNro", "ar");
                    docNro.addTextNode("0");

                } else {
                    docTipo.addTextNode("80");
                    SOAPElement docNro = feDetReqArray.addChildElement("DocNro", "ar");
                    docNro.addTextNode(billRequest.getCuit());
                }
            } else {
                docTipo.addTextNode("96");
                SOAPElement docNro = feDetReqArray.addChildElement("DocNro", "ar");
                docNro.addTextNode(billRequest.getCuit());
            }

            SOAPElement cbteDesde = feDetReqArray.addChildElement("CbteDesde", "ar");
            cbteDesde.addTextNode(String.valueOf(lastBillIdResponse.nextBillId()));
            SOAPElement cbteHasta = feDetReqArray.addChildElement("CbteHasta", "ar");
            cbteHasta.addTextNode(String.valueOf(lastBillIdResponse.nextBillId()));
            SOAPElement impTotalIVA = feDetReqArray.addChildElement("CbteFch", "ar");
            impTotalIVA.addTextNode(formatterBill.format(billRequest.getDate()));
            SOAPElement impTotal = feDetReqArray.addChildElement("ImpTotal", "ar");
            if (billRequest.getBillType().equals(BillType.B) || billRequest.getBillType().equals(BillType.A)) {
                impTotal.addTextNode(String.valueOf(billRequest.getTotalAmount() * 1.21)); // 21%
            } else {
                impTotal.addTextNode(String.valueOf(billRequest.getTotalAmount()));
            }

            SOAPElement impTotConc = feDetReqArray.addChildElement("ImpTotConc", "ar");
            impTotConc.addTextNode("0");

            // Si ImpNeto es mayor a 0 el objeto IVA es obligatorio
            SOAPElement impNeto = feDetReqArray.addChildElement("ImpNeto", "ar");
            impNeto.addTextNode(String.valueOf(billRequest.getTotalAmount()));
            SOAPElement impOpEx = feDetReqArray.addChildElement("ImpOpEx", "ar");
            impOpEx.addTextNode("0");
            SOAPElement impTrib = feDetReqArray.addChildElement("ImpTrib", "ar");
            impTrib.addTextNode("0");
            SOAPElement impIVAPerc = feDetReqArray.addChildElement("ImpIVA", "ar");

            if (billRequest.getBillType().equals(BillType.B) || billRequest.getBillType().equals(BillType.A)) {
                impIVAPerc.addTextNode(String.valueOf(billRequest.getTotalAmount() * 0.21));
            } else {
                impIVAPerc.addTextNode("0");
            }
            // FchServDesde, FchServHasta, FchVtoPago Debe informarse solo si Concepto es igual a 2 o 3
            //SOAPElement fechaDesde = feDetReqArray.addChildElement("FchServDesde", "ar");
            //fechaDesde.addTextNode(formatterBill.format(billRequest.getDate()));
            //SOAPElement impTotalIVAPerc = feDetReqArray.addChildElement("FchServHasta", "ar");
            //impTotalIVAPerc.addTextNode(formatterBill.format(billRequest.getDate()));
            //SOAPElement impTribPercep = feDetReqArray.addChildElement("FchVtoPago", "ar");
            //impTribPercep.addTextNode(formatterBill.format(billRequest.getDate()));
            SOAPElement impMonOrig = feDetReqArray.addChildElement("MonId", "ar");
            impMonOrig.addTextNode("PES");
            SOAPElement impMonCotiz = feDetReqArray.addChildElement("MonCotiz", "ar");
            impMonCotiz.addTextNode("1");
            //Comprobantes Asociados
            // Debera informar CbtesAsoc solo si el CbteTipo que se informa es igual a 1, 2, 3, 6, 7, 8, 12, 13, 51, 52, 53, 201, 202, 203, 206, 207, 208, 211, 212, 213
            //SOAPElement cbtesAsoc = feDetReqArray.addChildElement("CbtesAsoc", "ar");
            //SOAPElement cbteAsoc = cbtesAsoc.addChildElement("CbteAsoc", "ar");
            //SOAPElement cbteAsocTipo = cbteAsoc.addChildElement("Tipo", "ar");
            //cbteAsocTipo.addTextNode(String.valueOf(billRequest.getBillType().getCode()));
            //SOAPElement cbteAsocPtoVta = cbteAsoc.addChildElement("PtoVta", "ar");
            //cbteAsocPtoVta.addTextNode(String.valueOf(billRequest.getPuntoDeVenta()));
            //SOAPElement cbteAsocNro = cbteAsoc.addChildElement("Nro", "ar");
            //cbteAsocNro.addTextNode(String.valueOf(lastBillIdResponse.nextBillId()));
            //SOAPElement cbtesAsocCbteFch = cbteAsoc.addChildElement("Cuit", "ar");
            //cbtesAsocCbteFch.addTextNode(billRequest.getCuit());
            //SOAPElement cbteAsocCbteFch = cbteAsoc.addChildElement("CbteFch", "ar");
            //cbteAsocCbteFch.addTextNode(formatterBill.format(LocalDateTime.now()));

            //Iva
            if (billRequest.getBillType().equals(BillType.B) || billRequest.getBillType().equals(BillType.A)) {
                SOAPElement iva = feDetReqArray.addChildElement("Iva", "ar");
                SOAPElement ivaAlicIva = iva.addChildElement("AlicIva", "ar");
                SOAPElement ivaId = ivaAlicIva.addChildElement("Id", "ar");
                ivaId.addTextNode("5");
                SOAPElement ivaBaseImp = ivaAlicIva.addChildElement("BaseImp", "ar");
                ivaBaseImp.addTextNode(String.valueOf(billRequest.getTotalAmount()));
                SOAPElement ivaImporte = ivaAlicIva.addChildElement("Importe", "ar");
                ivaImporte.addTextNode(String.valueOf(billRequest.getTotalAmount() * 0.21));
            }
            return soapMessage;


        } catch (SOAPException e) {
            e.printStackTrace();
            logger.error("Error al crear el mensaje SOAP para el CAE", e);
            return null;
        }
    }


    public static SOAPMessage createGetTiposCbte(final LoginTicketResponse ticketResponse) {
        try {
            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("ar", "http://ar.gov.afip.dif.FEV1/");
            SOAPBody soapBody = envelope.getBody();
            SOAPElement soapBodyElem = soapBody.addChildElement("FEParamGetTiposCbte", "ar");
            SOAPElement auth = soapBodyElem.addChildElement("Auth", "ar");
            SOAPElement tokenElement = auth.addChildElement("Token", "ar");
            tokenElement.addTextNode(ticketResponse.getToken());
            SOAPElement signElement = auth.addChildElement("Sign", "ar");
            signElement.addTextNode(ticketResponse.getSign());
            SOAPElement cuitElement = auth.addChildElement("Cuit", "ar");
            cuitElement.addTextNode("20285640661");

            return soapMessage;

        } catch (SOAPException e) {
            e.printStackTrace();
            logger.error("Error al crear el mensaje SOAP para el CAE", e);
            return null;
        }
    }
}

