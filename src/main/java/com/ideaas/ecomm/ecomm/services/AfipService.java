package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.converts.AfipConvert;
import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.payload.LoginTicket;
import com.ideaas.ecomm.ecomm.services.interfaces.IAfipService;
import com.ideaas.ecomm.ecomm.services.interfaces.ILoginTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AfipService  implements IAfipService {

    private static final Logger logger = LoggerFactory.getLogger(AfipService.class);

    @Value("${certificatedPath}")
    private String certificatedPath;

    private AfipWSAAClient client;

    private ILoginTicketService loginTicketService;

    @Autowired
    public AfipService(final ILoginTicketService loginTicketService) {
        this.loginTicketService = loginTicketService;
        this.client = new AfipWSAAClient();
    }

    @Override
    public LoginTicketResponse get(final String service){
        Optional<LoginTicketResponse> loginTicket = loginTicketService.getActive(service);
        if(loginTicket.isPresent()){
            return loginTicket.get();

        } else {
            LoginTicketResponse loginTicketResponse = getAuthentication(service);
            loginTicketService.save(loginTicketResponse);

            return loginTicketResponse;
        }
    }

    @Override
    public LoginTicketResponse getAuthentication(final String service) {
        System.setProperty("http.proxyHost", "");
        System.setProperty("http.proxyPort", "80");
        final String endpoint = "https://wsaahomo.afip.gov.ar/ws/services/LoginCms";
        final String dstDN = "CN=wsaahomo, O=AFIP, C=AR, SERIALNUMBER=CUIT 33693450239";
        final String p12file = certificatedPath;
        final String signer = "fedeberon";
        final String p12pass = "1234";
        final byte[] LoginTicketRequest_xml_cms = client.create_cms(p12file, p12pass, signer, dstDN, service);
        final String result = client.invokeWSAA(LoginTicketRequest_xml_cms, endpoint);
        logger.info("LoginTicketRequest_xml_cms: {}", result);

        final LoginTicket loginTicketResponse = AfipConvert.convertToLoginTicketResponse(result);

        return loginTicketResponse.build();
    }


}
