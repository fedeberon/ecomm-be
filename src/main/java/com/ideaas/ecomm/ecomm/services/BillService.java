package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.payload.AFIP.FECAE;
import com.ideaas.ecomm.ecomm.payload.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.payload.AFIP.Person;
import com.ideaas.ecomm.ecomm.payload.AFIP.PersonPayload;
import com.ideaas.ecomm.ecomm.services.interfaces.IBillService;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BillService implements IBillService {

    public static String AFIP_A5_SERVICE = "https://aws.afip.gov.ar/sr-padron/webservices/personaServiceA5";
    public static String AFIP_FACTURACION_SERVICE = "https://wswhomo.afip.gov.ar/wsfev1/service.asmx?op=FECAESolicitar";

    @Override
    public Person createPersonRequest(final String token,
                                      final String sign,
                                      final String cuitRepresentada,
                                      final String idPersona) {
        try {
            java.net.URL endPoint = new java.net.URL(AFIP_FACTURACION_SERVICE);
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(token, sign, cuitRepresentada, idPersona), endPoint);
            String result = printSOAPResponse(soapResponse);
            PersonPayload personPayload = convert(result);
            soapConnection.close();

            return personPayload.getPerson();
        }catch (Exception e) {
            System.out.print("ERROR DE PARSEO ===========> " + e);
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public FECAE createCAERequest(final LoginTicketResponse ticketResponse,
                                  final String CUIT) {
        try {
            java.net.URL endPoint = new java.net.URL(AFIP_FACTURACION_SERVICE);
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            SOAPMessage soapResponse = soapConnection.call(prepareCAE(ticketResponse, CUIT), endPoint);
            String result = printSOAPResponse(soapResponse);
            FECAE cae = convertToCAE(result);
            soapConnection.close();

            return cae;

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SOAPMessage createSOAPRequest(final String token,
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

    private String printSOAPResponse(SOAPMessage soapResponse) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
        javax.xml.transform.Source sourceContent = soapResponse.getSOAPPart().getContent();
        StringWriter outWriter = new StringWriter();
        javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(outWriter);
        transformer.transform(sourceContent, result);
        StringBuffer sb = outWriter.getBuffer();
        String finalstring = sb.toString();

        return finalstring;
    }

    public PersonPayload convert(String xml) throws JAXBException, XMLStreamException {
        JAXBContext jc = JAXBContext.newInstance(new Class[] { PersonPayload.class });
        StringReader reader = new StringReader(xml);
        XMLInputFactory xif = XMLInputFactory.newFactory();
        XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);
        xmlReader.nextTag();
        while (!xmlReader.getLocalName().equals("personaReturn")) {
            xmlReader.nextTag();
        }
        javax.xml.bind.Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        javax.xml.bind.JAXBElement<PersonPayload> jb = jaxbUnmarshaller.unmarshal(xmlReader, PersonPayload.class);
        xmlReader.close();
        PersonPayload personaReturn = jb.getValue();

        return personaReturn;
    }

    public FECAE convertToCAE(String xml) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
        JAXBContext jc = JAXBContext.newInstance(new Class[] { FECAE.class });
        StringReader reader = new StringReader(xml);
        XMLInputFactory xif = XMLInputFactory.newFactory();
        XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);
        xmlReader.nextTag();
        while (!xmlReader.getLocalName().equals("FECAESolicitarResult")) {
            xmlReader.nextTag();
        }
        javax.xml.bind.Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        javax.xml.bind.JAXBElement<FECAE> jb = jaxbUnmarshaller.unmarshal(xmlReader, FECAE.class);
        xmlReader.close();
        FECAE fecae = jb.getValue();

        return fecae;
    }


    @SuppressWarnings("all")
    private static SOAPMessage createBillSOAPRequest(final String token,
                                                 final String sign,
                                                 final String cuitRepresentada,
                                                 final String idPersona) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String serverURI = "http://a5.soap.ws.server.puc.sr/";
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ser", AFIP_FACTURACION_SERVICE);
        javax.xml.soap.SOAPBody soapBody = envelope.getBody();

        SOAPElement autorizarComprobanteRequestElement = soapBody.addChildElement("autorizarComprobanteRequest", "ser");

        SOAPElement authRequestElement = autorizarComprobanteRequestElement.addChildElement("authRequest");

        SOAPElement tokenElement = authRequestElement.addChildElement("token");
        tokenElement.addTextNode(token);

        SOAPElement signElement = authRequestElement.addChildElement("sign");
        signElement.addTextNode(sign);

        SOAPElement cuitRepresentadaElement = authRequestElement.addChildElement("cuitRepresentada");
        authRequestElement.addTextNode(cuitRepresentada);

        SOAPElement comprobanteCAERequestElement = autorizarComprobanteRequestElement.addChildElement("comprobanteCAERequest");
        authRequestElement.addTextNode(cuitRepresentada);

        SOAPElement codigoTipoComprobanteElement = autorizarComprobanteRequestElement.addChildElement("codigoTipoComprobante");
        codigoTipoComprobanteElement.addTextNode(cuitRepresentada);

        SOAPElement numeroPuntoVentaElement = autorizarComprobanteRequestElement.addChildElement("numeroPuntoVenta");
        numeroPuntoVentaElement.addTextNode(cuitRepresentada);

        SOAPElement numeroComprobanteElement = autorizarComprobanteRequestElement.addChildElement("numeroComprobante");
        numeroComprobanteElement.addTextNode(cuitRepresentada);

        SOAPElement fechaEmisionElement = autorizarComprobanteRequestElement.addChildElement("fechaEmision");
        fechaEmisionElement.addTextNode(cuitRepresentada);

        SOAPElement codigoTipoDocumentoElement = autorizarComprobanteRequestElement.addChildElement("codigoTipoDocumento");
        codigoTipoDocumentoElement.addTextNode(cuitRepresentada);

        SOAPElement numeroDocumentoElement = autorizarComprobanteRequestElement.addChildElement("numeroDocumento");
        numeroDocumentoElement.addTextNode(cuitRepresentada);

        SOAPElement importeGravadoElement = autorizarComprobanteRequestElement.addChildElement("importeGravado");
        importeGravadoElement.addTextNode(cuitRepresentada);

        SOAPElement importeNoGravadoElement = autorizarComprobanteRequestElement.addChildElement("importeNoGravado");
        importeNoGravadoElement.addTextNode(cuitRepresentada);

        SOAPElement importeExentoElement = autorizarComprobanteRequestElement.addChildElement("importeExento");
        importeExentoElement.addTextNode(cuitRepresentada);

        SOAPElement importeSubtotalElement = autorizarComprobanteRequestElement.addChildElement("importeSubtotal");
        importeSubtotalElement.addTextNode(cuitRepresentada);

        SOAPElement importeOtrosTributosElement = autorizarComprobanteRequestElement.addChildElement("importeOtrosTributos");
        importeOtrosTributosElement.addTextNode(cuitRepresentada);

        SOAPElement importeTotalElement = autorizarComprobanteRequestElement.addChildElement("importeTotal");
        importeTotalElement.addTextNode(cuitRepresentada);

        SOAPElement codigoMonedaElement = autorizarComprobanteRequestElement.addChildElement("codigoMoneda");
        codigoMonedaElement.addTextNode(cuitRepresentada);

        SOAPElement cotizacionMonedaElement = autorizarComprobanteRequestElement.addChildElement("cotizacionMoneda");
        cotizacionMonedaElement.addTextNode(cuitRepresentada);

        SOAPElement observacionesElement = autorizarComprobanteRequestElement.addChildElement("observaciones");
        observacionesElement.addTextNode(cuitRepresentada);

        SOAPElement codigoConceptoElement = autorizarComprobanteRequestElement.addChildElement("codigoConcepto");
        codigoConceptoElement.addTextNode(cuitRepresentada);

        SOAPElement arrayItemsElement = autorizarComprobanteRequestElement.addChildElement("arrayItems");
        arrayItemsElement.addTextNode(cuitRepresentada);

        SOAPElement itemElement1 = autorizarComprobanteRequestElement.addChildElement("item");
        itemElement1.addTextNode(cuitRepresentada);
        SOAPElement itemElement2 = autorizarComprobanteRequestElement.addChildElement("item");
        itemElement1.addTextNode(cuitRepresentada);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI + "getPersona");
        soapMessage.saveChanges();


        return soapMessage;
    }

    @SuppressWarnings("all")
    @Override
    public SOAPMessage prepareCAE(LoginTicketResponse response,
                                  String CUIT) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String serverURI = "https://wswhomo.afip.gov.ar/wsfev1/service.asmx?op=FECAESolicitar";
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ser", serverURI);
        javax.xml.soap.SOAPBody soapBody = envelope.getBody();

        SOAPElement FECAESolicitarElement = soapBody.addChildElement("FECAESolicitar");

        SOAPElement authElement = FECAESolicitarElement.addChildElement("Auth");
        SOAPElement tokenElement = authElement.addChildElement("Token");
        tokenElement.setTextContent(response.getToken());
        SOAPElement signElement = authElement.addChildElement("Sign");
        signElement.setTextContent(response.getSign());
        SOAPElement cuitElement = authElement.addChildElement("Cuit");
        cuitElement.setTextContent(CUIT);

        SOAPElement feCAEReqElement = soapBody.addChildElement("FeCAEReq");
        SOAPElement feCabReqElement = feCAEReqElement.addChildElement("FeCabReq");
        SOAPElement cantRegElement = feCabReqElement.addChildElement("CantReg");
        cantRegElement.setTextContent("1");

        SOAPElement ptoVtaElement = feCabReqElement.addChildElement("PtoVta");
        ptoVtaElement.setTextContent("1");
        SOAPElement cbteTipoElement = feCabReqElement.addChildElement("CbteTipo");
        cbteTipoElement.setTextContent("6");

        SOAPElement FeDetReqElement = feCAEReqElement.addChildElement("FeCAEReq");
        SOAPElement FECAEDetRequestElement = FeDetReqElement.addChildElement("FECAEDetRequest");
        SOAPElement ConceptoElement = FECAEDetRequestElement.addChildElement("Concepto");

        SOAPElement docTipoElement = FECAEDetRequestElement.addChildElement("DocTipo");
        docTipoElement.setTextContent("80");
        SOAPElement docNroElement = FECAEDetRequestElement.addChildElement("DocNro");
        docNroElement.setTextContent("");
        SOAPElement cbteDesdeElement = FECAEDetRequestElement.addChildElement("CbteDesde");
        cbteDesdeElement.setTextContent("1");
        SOAPElement cbteHastaElement = FECAEDetRequestElement.addChildElement("CbteHasta");
        cbteHastaElement.setTextContent("1");
        SOAPElement cbteFchElement = FECAEDetRequestElement.addChildElement("CbteFch");
        cbteFchElement.setTextContent(formatter.format(LocalDateTime.now()));

        SOAPElement ImpTotalElement = FECAEDetRequestElement.addChildElement("ImpTotal");
        ImpTotalElement.setTextContent("184.05");
        SOAPElement impTotConcElement = FECAEDetRequestElement.addChildElement("ImpTotConc");
        impTotConcElement.setTextContent("0");
        SOAPElement impNetoElement = FECAEDetRequestElement.addChildElement("ImpNeto");
        impNetoElement.setTextContent("150");
        SOAPElement impOpExElement = FECAEDetRequestElement.addChildElement("ImpOpEx");
        impOpExElement.setTextContent("0");
        SOAPElement impIVAElement = FECAEDetRequestElement.addChildElement("ImpIVA");
        impIVAElement.setTextContent("26.25");
        SOAPElement impTribElement = FECAEDetRequestElement.addChildElement("ImpTrib");
        impTribElement.setTextContent("7.8");

        SOAPElement fchServDesdeElement = FECAEDetRequestElement.addChildElement("FchServDesde");
        fchServDesdeElement.setTextContent(null);
        SOAPElement fchServHastaElement = FECAEDetRequestElement.addChildElement("FchServHasta");
        fchServHastaElement.setTextContent(null);
        SOAPElement fchVtoPagoElement = FECAEDetRequestElement.addChildElement("FchVtoPago");
        fchVtoPagoElement.setTextContent(null);
        SOAPElement monIdElement = FECAEDetRequestElement.addChildElement("MonId");
        monIdElement.setTextContent("PES");
        SOAPElement monCotizElement = FECAEDetRequestElement.addChildElement("MonCotiz");
        monCotizElement.setTextContent("1");

        return soapMessage;
    }
}
