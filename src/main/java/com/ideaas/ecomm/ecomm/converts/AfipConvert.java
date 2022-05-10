package com.ideaas.ecomm.ecomm.converts;

import com.ideaas.ecomm.ecomm.converts.exceptions.Fault;
import com.ideaas.ecomm.ecomm.payload.BillResponse;
import com.ideaas.ecomm.ecomm.payload.CAEAResponse;
import com.ideaas.ecomm.ecomm.payload.Err;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;
import com.ideaas.ecomm.ecomm.payload.LoginTicket;
import com.ideaas.ecomm.ecomm.payload.PersonPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;

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

    /**
     * It converts the xml to a Fault object.
     * @param xml as {@link String} with result fron AFIP.
     * @return {@link Fault} object.
     * @throws JAXBException
     * @throws XMLStreamException
     */
    public static Fault hasFault(final String xml) throws JAXBException, XMLStreamException {
        JAXBContext jc = JAXBContext.newInstance(new Class[] { Fault.class });
        StringReader reader = new StringReader(xml);
        XMLInputFactory xif = XMLInputFactory.newFactory();
        XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);
        xmlReader.nextTag();
        boolean hasFault = false;
        while (!xmlReader.getLocalName().equals("Fault") && !xmlReader.isEndElement()) {
            try {
                xmlReader.nextTag();
            } catch (Exception e) {
                xmlReader.nextTag();
            }
        }
        javax.xml.bind.Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        javax.xml.bind.JAXBElement<Fault> jb = null;
        try {
            jb = jaxbUnmarshaller.unmarshal(xmlReader, Fault.class);
        } catch (IllegalStateException e) {
            e.printStackTrace();

            return null;
        }
        xmlReader.close();
        Fault personaReturn = jb.getValue();

        return personaReturn;

    }


    public static LastBillIdResponse convertoToLastBillId(String xml) {
        try {
            xml = xml.replace("soap:", "");
            xml = xml.replace("xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"", "");
            xml = xml.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
            xml = xml.replace("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"", "");
            xml = xml.replace("xmlns=\"http://ar.gov.afip.dif.FEV1/\"", "");
            xml = xml.replace("xmlns=\"http://ar.gov.afip.dif.FEV1/\"", "");

            JAXBContext jc = JAXBContext.newInstance(new Class[] { LastBillIdResponse.class });
            StringReader reader = new StringReader(xml);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);
            xmlReader.nextTag();
            while (!xmlReader.getLocalName().equals("FECompUltimoAutorizadoResult")) {
               logger.info(xmlReader.getLocalName());
               try {
                   xmlReader.nextTag();
               } catch (XMLStreamException e) {
                   xmlReader.nextTag();
               }
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

    public static BillResponse convertoToBillResponse(String xml) {
        try {
            xml = xml.replace("soap:", "");
            xml = xml.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
            xml = xml.replace(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"", "");
            xml = xml.replace("xmlns=\"http://ar.gov.afip.dif.FEV1/\"", "");
            xml = xml.replace("xmlns=\"http://ar.gov.afip.dif.FEV1/\"", "");
            JAXBContext jc = JAXBContext.newInstance(new Class[] { BillResponse.class });
            StringReader reader = new StringReader(xml);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);
            xmlReader.nextTag();

            Err tagName = hasErrors(xml, "Err");
            if (Objects.isNull(tagName)) {
                tagName = hasErrors(xml, "Obs");
            }


            while (!xmlReader.getLocalName().equals("FECAEDetResponse")) {
                try {
                    xmlReader.nextTag();
                } catch (XMLStreamException e) {
                    xmlReader.nextTag();
                }
            }

            javax.xml.bind.Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
            javax.xml.bind.JAXBElement<BillResponse> jb = jaxbUnmarshaller.unmarshal(xmlReader, BillResponse.class);
            xmlReader.close();
            BillResponse billResponse = jb.getValue();
            billResponse.setMessage(tagName);

            return billResponse;
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Err hasErrors(String xml, String tagName) {
        try {
            xml = xml.replace("soap:", "");
            xml = xml.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
            xml = xml.replace(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"", "");
            xml = xml.replace("xmlns=\"http://ar.gov.afip.dif.FEV1/\"", "");
            xml = xml.replace("xmlns=\"http://ar.gov.afip.dif.FEV1/\"", "");
            JAXBContext jc = JAXBContext.newInstance(new Class[] { Err.class });
            StringReader reader = new StringReader(xml);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);
            xmlReader.nextTag();

            while (!xmlReader.getLocalName().equals(tagName)) {
                try {
                    System.out.println(xmlReader.getLocalName());
                    xmlReader.nextTag();
                } catch (Exception e) {
                    xmlReader.nextTag();
                }
            }

        javax.xml.bind.Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        javax.xml.bind.JAXBElement<Err> jb = jaxbUnmarshaller.unmarshal(xmlReader, Err.class);
        xmlReader.close();
        return jb.getValue();

        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static BillResponse convertoToBill(String xml) {
        try {
            xml = xml.replace("soap:", "");
            xml = xml.replace("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", "");
            xml = xml.replace(" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"", "");
            xml = xml.replace("xmlns=\"http://ar.gov.afip.dif.FEV1/\"", "");
            xml = xml.replace("xmlns=\"http://ar.gov.afip.dif.FEV1/\"", "");
            JAXBContext jc = JAXBContext.newInstance(new Class[] { BillResponse.class });
            StringReader reader = new StringReader(xml);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);
            xmlReader.nextTag();

            while (!xmlReader.getLocalName().equals("FECAEDetResponse")) {
                try {
                    xmlReader.nextTag();
                } catch (XMLStreamException e) {
                    xmlReader.nextTag();
                }
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
            logger.info("XML recieved: {}", xml);
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
            logger.error("XMLStreamException: {}", e);
            e.printStackTrace();
        } catch (JAXBException e) {
            logger.error("JAXBException: {}", e);
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getJavaObjectFromSoapXml(String data, Class<T> clazz) {
        try {
            XMLInputFactory xif = XMLInputFactory.newFactory();
            StringReader reader = new StringReader(data);
            XMLStreamReader xsr = xif.createXMLStreamReader(reader);
            xsr.nextTag();
            while (!xsr.getLocalName().equalsIgnoreCase(clazz.getSimpleName())) {
                logger.info("XML recieved: {}", xsr.getLocalName());
                xsr.nextTag();
            }

            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jc.createUnmarshaller();

            JAXBElement<T> je = unmarshaller.unmarshal(xsr, clazz);

            return je.getValue();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
