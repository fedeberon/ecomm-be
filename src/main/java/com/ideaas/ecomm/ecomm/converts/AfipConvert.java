package com.ideaas.ecomm.ecomm.converts;

import com.ideaas.ecomm.ecomm.payload.BillResponse;
import com.ideaas.ecomm.ecomm.payload.CAEAResponse;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;
import com.ideaas.ecomm.ecomm.payload.LoginTicket;
import com.ideaas.ecomm.ecomm.payload.PersonPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import java.io.StringReader;
import java.io.StringWriter;

@SuppressWarnings("all")
public class AfipConvert {

    private static final Logger logger = LoggerFactory.getLogger(AfipConvert.class);
    @SuppressWarnings("all")
    public static CAEAResponse convertToCAE(final String xml) {
        try {
            logger.info("XML recieved: {}", xml);
            JAXBContext jc = JAXBContext.newInstance(new Class[] { CAEAResponse.class });
            StringReader reader = new StringReader(xml);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);
            xmlReader.nextTag();
            while (!xmlReader.getLocalName().equals("CAEAResponse")) {
                xmlReader.nextTag();
            }
            javax.xml.bind.Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
            javax.xml.bind.JAXBElement<CAEAResponse> jb = jaxbUnmarshaller.unmarshal(xmlReader, CAEAResponse.class);
            xmlReader.close();
            CAEAResponse fecae = jb.getValue();
            logger.info("CAEAResponse: {}", fecae);

            return fecae;
            } catch (JAXBException e) {
                logger.error("JAXBException: {}", e);
                e.printStackTrace();
            } catch (XMLStreamException e) {
                logger.error("XMLStreamException: {}", e);
                e.printStackTrace();
            }
        return null;
    }

    public static PersonPayload convertToPersonPayload(final String xml) throws JAXBException, XMLStreamException {
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

    public static LastBillIdResponse convertoToLastBillId(final String xml) {
        try {
            JAXBContext jc = JAXBContext.newInstance(new Class[] { LastBillIdResponse.class });
            StringReader reader = new StringReader(xml);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);

            xmlReader.nextTag();
            while (!xmlReader.getLocalName().equals("consultarUltimoComprobanteAutorizadoResponse")) {
                xmlReader.nextTag();
            }
            javax.xml.bind.Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
            javax.xml.bind.JAXBElement<LastBillIdResponse> jb = jaxbUnmarshaller.unmarshal(xmlReader, LastBillIdResponse.class);
            xmlReader.close();
            LastBillIdResponse lastBillId = jb.getValue();

            return lastBillId;
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BillResponse convertoToBillResponse(final String xml) {
        try {
            JAXBContext jc = JAXBContext.newInstance(new Class[] { BillResponse.class });
            StringReader reader = new StringReader(xml);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);

            xmlReader.nextTag();
            while (!xmlReader.getLocalName().equals("autorizarComprobanteResponse")) {
                xmlReader.nextTag();
            }
            javax.xml.bind.Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
            javax.xml.bind.JAXBElement<BillResponse> jb = jaxbUnmarshaller.unmarshal(xmlReader, BillResponse.class);
            xmlReader.close();
            BillResponse billResponse = jb.getValue();

            return billResponse;
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String printSOAPResponse(SOAPMessage soapResponse) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();

            javax.xml.transform.Source sourceContent = soapResponse.getSOAPPart().getContent();
            StringWriter outWriter = new StringWriter();
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(outWriter);
            transformer.transform(sourceContent, result);
            StringBuffer sb = outWriter.getBuffer();
            String finalstring = sb.toString();

            return finalstring;

            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (SOAPException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }

        return null;
    }

    public static LoginTicket convertToLoginTicketResponse(final String xml) {
        try {

            JAXBContext jc = JAXBContext.newInstance(new Class[] { LoginTicket.class });
            StringReader reader = new StringReader(xml);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);

            xmlReader.nextTag();
            while (!xmlReader.getLocalName().equals("loginTicketResponse")) {
                xmlReader.nextTag();
            }
            javax.xml.bind.Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
            javax.xml.bind.JAXBElement<LoginTicket> jb = jaxbUnmarshaller.unmarshal(xmlReader, LoginTicket.class);
            xmlReader.close();
            LoginTicket loginTicketResponse = jb.getValue();

            return loginTicketResponse;
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
