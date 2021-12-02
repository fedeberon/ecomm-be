package com.ideaas.ecomm.ecomm.converts;

import com.ideaas.ecomm.ecomm.converts.exceptions.Errors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

public class AfipExceptionConvert {

    public static Errors convertToErrorAfip(final String xml) {
        try {
        JAXBContext jc = JAXBContext.newInstance(new Class[] { Errors.class });
        StringReader reader = new StringReader(xml);
        XMLInputFactory xif = XMLInputFactory.newFactory();
        XMLStreamReader xmlReader = xif.createXMLStreamReader(reader);
        xmlReader.nextTag();
        while (!xmlReader.getLocalName().equals("arrayErrores")) {
            xmlReader.nextTag();
        }
        javax.xml.bind.Unmarshaller jaxbUnmarshaller = jc.createUnmarshaller();
        javax.xml.bind.JAXBElement<Errors> jb = jaxbUnmarshaller.unmarshal(xmlReader, Errors.class);
        xmlReader.close();
        Errors errors = jb.getValue();

        return errors;
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
