package com.ideaas.ecomm.ecomm.services;

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
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;

@Service
public class BillService implements IBillService {

    public static String AFIP_A5_SERVICE = "https://aws.afip.gov.ar/sr-padron/webservices/personaServiceA5";
    public static String AFIP_FACTURACION_SERVICE = "http://impl.service.wsmtxca.afip.gov.ar/service/";

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
            System.out.print(personPayload);
            soapConnection.close();

            return personPayload.getPerson();
        } catch (SAXException e) {
            e.printStackTrace();
            System.out.print("ERROR DE PARSEO ===========> " + e);
            return null;
        } catch (XMLStreamException e) {
            e.printStackTrace();
            System.out.print("ERROR DE PARSEO ===========> " + e);
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.print("ERROR DE PARSEO ===========> " + e);
            return null;
        } catch (ParserConfigurationException e) {
            System.out.print("ERROR DE PARSEO ===========> " + e);
            e.printStackTrace();
            System.out.print("ERROR DE PARSEO ===========> " + e);
            return null;
        } catch (JAXBException e) {
            System.out.print("ERROR DE PARSEO ===========> " + e);
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.print("ERROR DE PARSEO ===========> " + e);
            e.printStackTrace();
            return null;
        } catch (SOAPException e) {
            System.out.print("ERROR DE PARSEO ===========> " + e);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.out.print("ERROR DE PARSEO ===========> " + e);
            e.printStackTrace();
        }
        return null;
    }

    private static SOAPMessage createSOAPRequest(final String token,
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

    public PersonPayload convert(String xml) throws ParserConfigurationException, IOException, SAXException, JAXBException, XMLStreamException {
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

    @SuppressWarnings("all")
    private static SOAPMessage getCAR() throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String serverURI = "https://wswhomo.afip.gov.ar/wsfev1/service.asmx?op=FECAESolicitar";
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ser", AFIP_FACTURACION_SERVICE);
        javax.xml.soap.SOAPBody soapBody = envelope.getBody();

        SOAPElement FECAESolicitarElement = soapBody.addChildElement("FECAESolicitar", "ar");

        SOAPElement authElement = FECAESolicitarElement.addChildElement("Auth", "ar");
        SOAPElement tokenElement = authElement.addChildElement("Token", "ar");
        SOAPElement signElement = authElement.addChildElement("Sign", "ar");
        SOAPElement cuitElement = authElement.addChildElement("Cuit", "ar");

        SOAPElement FeCAEReqElement = soapBody.addChildElement("FeCAEReq", "ar");
        SOAPElement FeCabReqElement = FeCAEReqElement.addChildElement("FeCabReq", "ar");
        SOAPElement CantRegElement = FeCabReqElement.addChildElement("CantReg", "ar");
        SOAPElement PtoVtaElement = FeCabReqElement.addChildElement("PtoVta", "ar");
        SOAPElement CbteTipoElement = FeCabReqElement.addChildElement("CbteTipo", "ar");


        SOAPElement FeDetReqElement = FeCAEReqElement.addChildElement("FeCAEReq", "ar");
        SOAPElement FECAEDetRequestElement = FeDetReqElement.addChildElement("FECAEDetRequest", "ar");
        SOAPElement ConceptoElement = FECAEDetRequestElement.addChildElement("Concepto", "ar");
        SOAPElement DocTipoElement = FECAEDetRequestElement.addChildElement("DocTipo", "ar");
        SOAPElement DocNroElement = FECAEDetRequestElement.addChildElement("DocNro", "ar");
        SOAPElement CbteDesdeElement = FECAEDetRequestElement.addChildElement("CbteDesde", "ar");
        SOAPElement CbteHastaElement = FECAEDetRequestElement.addChildElement("CbteHasta", "ar");
        SOAPElement CbteFchElement = FECAEDetRequestElement.addChildElement("CbteFch", "ar");
        SOAPElement ImpTotalElement = FECAEDetRequestElement.addChildElement("ImpTotal", "ar");
        SOAPElement ImpTotConcElement = FECAEDetRequestElement.addChildElement("ImpTotConc", "ar");
        SOAPElement ImpNetoElement = FECAEDetRequestElement.addChildElement("ImpNeto", "ar");
        SOAPElement ImpOpExElement = FECAEDetRequestElement.addChildElement("ImpOpEx", "ar");
        SOAPElement ImpTribElement = FECAEDetRequestElement.addChildElement("ImpTrib", "ar");
        SOAPElement ImpIVAElement = FECAEDetRequestElement.addChildElement("ImpIVA", "ar");
        SOAPElement FchServDesdeElement = FECAEDetRequestElement.addChildElement("FchServDesde", "ar");
        SOAPElement FchServHastaElement = FECAEDetRequestElement.addChildElement("FchServHasta", "ar");
        SOAPElement FchVtoPagoElement = FECAEDetRequestElement.addChildElement("FchVtoPago", "ar");
        SOAPElement MonIdElement = FECAEDetRequestElement.addChildElement("MonId", "ar");
        SOAPElement MonCotizElement = FECAEDetRequestElement.addChildElement("MonCotiz", "ar");

        return soapMessage;
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
}
