package com.ideaas.ecomm.ecomm.domain.AFIP;

import com.ideaas.ecomm.ecomm.payload.LoginTicket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="CREDENCIAL_AUTENTICACION_AFIP")
public class LoginTicketResponse {

    @Id
    @Column(name="uniqueId")
    private String uniqueId;

    @Column(name="token")
    private String token;

    @Column(name = "sign")
    private String sign;

    @Column(name = "fechaDePedido")
    private LocalDateTime generationTime;

    @Column(name = "fechaDeCaducacion")
    private LocalDateTime expirationTime;

    public LoginTicketResponse(LoginTicket builder) {
        this.token = builder.getCredential().getToken();
        this.sign = builder.getCredential().getSign();
        this.generationTime = builder.getHeader().getGenerationTime();
    }
}


