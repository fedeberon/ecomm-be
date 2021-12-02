package com.ideaas.ecomm.ecomm.converts;

import com.ideaas.ecomm.ecomm.converts.exceptions.Fault;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

public class AfipValidationConverter {


    public static Fault convertToValidationAfip(final String xml) {
        try {
            JAXBContext jc = JAXBContext.newInstance(new Class[] { Fault.class });
            StringReader reader = new StringReader(xml);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);
            xmlReader.nextTag();
            while (!xmlReader.getLocalName().equals("Fault")) {
                xmlReader.nextTag();
            }
            javax.xml.bind.Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
            javax.xml.bind.JAXBElement<Fault> jb = jaxbUnmarshaller.unmarshal(xmlReader, Fault.class);
            xmlReader.close();
            Fault fault = jb.getValue();

            return fault;
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

}
