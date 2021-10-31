package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.payload.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.services.interfaces.IAfipService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AfipService  implements IAfipService {

    @Value("${certificatedPath}")
    private String certificatedPath;

    private AfipWSAAClient client;

    public AfipService() {
        this.client = new AfipWSAAClient();
    }

    @Override
    public LoginTicketResponse getAuthentication(final String service) {
        System.setProperty("http.proxyHost", "");
        System.setProperty("http.proxyPort", "80");
        String endpoint = "https://wsaahomo.afip.gov.ar/ws/services/LoginCms";
        String dstDN = "CN=wsaahomo, O=AFIP, C=AR, SERIALNUMBER=CUIT 33693450239";
        String p12file = certificatedPath;

        String signer = "fedeberon";
        String p12pass = "1234";

        byte[] LoginTicketRequest_xml_cms = client.create_cms(p12file, p12pass, signer, dstDN, service);
        String result = client.invokeWSAA(LoginTicketRequest_xml_cms, endpoint);
        Reader tokenReader = new StringReader(result);
        try {
            Document tokenDoc = new SAXReader(false).read(tokenReader);
            String uniqueId = tokenDoc.valueOf("/loginTicketResponse/header/uniqueId");
            String token = tokenDoc.valueOf("/loginTicketResponse/credentials/token");
            String sign = tokenDoc.valueOf("/loginTicketResponse/credentials/sign");
            String generationTime = tokenDoc.valueOf("/loginTicketResponse/header/generationTime");
            generationTime = generationTime.substring(0, 19);
            String expirationTime = tokenDoc.valueOf("/loginTicketResponse/header/expirationTime");
            expirationTime = expirationTime.substring(0, 19);
            LoginTicketResponse loginTicketResponse = new LoginTicketResponse(uniqueId, token, sign, generationTime, expirationTime);

            return loginTicketResponse;

        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("all")
    private static SOAPMessage createBillSOAPRequest(final String token,
                                                     final String sign,
                                                     final String cuitRepresentada,
                                                     final String idPersona) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String serverURI = "https://wswhomo.afip.gov.ar/wsfev1/service.asmx?op=FECAESolicitar";
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("ser", serverURI);
        javax.xml.soap.SOAPBody soapBody = envelope.getBody();

        SOAPElement autorizarComprobanteRequestElement = soapBody.addChildElement("autorizarComprobanteRequest", "ser");

        SOAPElement authRequestElement = autorizarComprobanteRequestElement.addChildElement("authRequest");

        SOAPElement tokenElement = authRequestElement.addChildElement("token");
        tokenElement.addTextNode(token);

        SOAPElement signElement = authRequestElement.addChildElement("sign");
        signElement.addTextNode(sign);

        SOAPElement cuitRepresentadaElement = authRequestElement.addChildElement("cuitRepresentada");
        authRequestElement.addTextNode("20285640661");

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
        headers.addHeader("SOAPAction", serverURI);
        soapMessage.saveChanges();


        return soapMessage;
    }



    @SuppressWarnings("all")
    public static SOAPMessage prepareCAE(LoginTicketResponse response,
                                  String CUIT) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String serverURI = "https://wswhomo.afip.gov.ar/wsfev1/service.asmx?op=FECAESolicitar";
        SOAPEnvelope envelope = soapPart.getEnvelope();
        javax.xml.soap.SOAPBody soapBody = envelope.getBody();

        SOAPElement FECAESolicitarElement = soapBody.addChildElement("FECAESolicitar");

        SOAPElement authElement = FECAESolicitarElement.addChildElement("Auth");
        SOAPElement tokenElement = authElement.addChildElement("Token");
        tokenElement.setTextContent(response.getToken());
        SOAPElement signElement = authElement.addChildElement("Sign");
        signElement.setTextContent(response.getSign());
        SOAPElement cuitElement = authElement.addChildElement("Cuit");
        cuitElement.setTextContent("20285640661");

        SOAPElement feCAEReqElement = soapBody.addChildElement("FeCAEReq");
        SOAPElement feCabReqElement = feCAEReqElement.addChildElement("FeCabReq");
        SOAPElement cantRegElement = feCabReqElement.addChildElement("CantReg");
        cantRegElement.setTextContent("1");
        SOAPElement ptoVtaElement = feCabReqElement.addChildElement("PtoVta");
        ptoVtaElement.setTextContent("1");
        SOAPElement cbteTipoElement = feCabReqElement.addChildElement("CbteTipo");
        cbteTipoElement.setTextContent("6");


        SOAPElement FeDetReqElement = feCAEReqElement.addChildElement("FeDetReq");
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

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", "http://ar.gov.afip.dif.FEV1/FECAESolicitar");
        soapMessage.saveChanges();

        return soapMessage;
    }
}
