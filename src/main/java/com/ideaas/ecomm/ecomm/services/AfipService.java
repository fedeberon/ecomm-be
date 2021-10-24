package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.payload.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.services.interfaces.IAfipService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.io.StringReader;

@Service
public class AfipService  implements IAfipService {

    @Value("${certificatedPath}")
    private String certificatedPath;

    private AfipWSAAClient client;

    public AfipService() {
        this.client = new AfipWSAAClient();
    }

    @Override
    public LoginTicketResponse getAuthentication() {
        System.setProperty("http.proxyHost", "");
        System.setProperty("http.proxyPort", "80");
        String endpoint = "https://wsaa.afip.gov.ar/ws/services/LoginCms";
        String dstDN = "CN=wsaa, O=AFIP, C=AR, SERIALNUMBER=CUIT 33693450239";
        String p12file = certificatedPath;

//        String service = "ws_sr_padron_a10";
        String service = "ws_sr_padron_a5";
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
}
