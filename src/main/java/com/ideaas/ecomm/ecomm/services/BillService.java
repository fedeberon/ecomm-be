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

    public static String ENDPOINT_PRODUCCION = "https://aws.afip.gov.ar/sr-padron/webservices/personaServiceA5";


    @Override
    public Person createPersonRequest(final String token,
                                      final String sign,
                                      final String cuitRepresentada,
                                      final String idPersona) {
        try {
            java.net.URL endPoint = new java.net.URL(ENDPOINT_PRODUCCION);
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
        System.out.print("============> " + xml);
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
}
