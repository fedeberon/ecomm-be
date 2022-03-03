package com.ideaas.ecomm.ecomm.domain.AFIP;

import com.ideaas.ecomm.ecomm.payload.LoginTicket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="CREDENCIAL_AUTENTICACION_AFIP")
public class LoginTicketResponse {

    @Id
    @Column(name="uniqueId")
    private String uniqueId;

    @Column(name="token", length = 999)
    private String token;

    @Column(name = "sign", length = 999)
    private String sign;

    @Column(name = "fechaDePedido")
    private LocalDateTime generationTime;

    @Column(name = "fechaDeCaducacion")
    private LocalDateTime expirationTime;

    @Column(name = "service")
    private String service;

    public LoginTicketResponse(LoginTicket builder) {
        this.uniqueId = builder.getHeader().getUniqueId();
        this.token = builder.getCredential().getToken();
        this.sign = builder.getCredential().getSign();
        this.generationTime = builder.getHeader().getGenerationTime();
    }
}


