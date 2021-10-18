package com.ideaas.ecomm.ecomm.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CALLBACK")
public class Callback implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CALL_ID")
    private Long id;

    @JsonProperty("collection_id")
    @Column(name = "CALL_COLLECTION_ID")
    private String collectionId;

    @JsonProperty("collection_status")
    @Column(name = "CALL_COLLECTION_STATUS")
    private String collectionStatus;

    @JsonProperty("payment_id")
    @Column(name = "CALL_PAYMENT_ID")
    private String paymentId;

    @JsonProperty("status")
    @Column(name = "CALL_STATUS")
    private String status;

    @JsonProperty("external_reference")
    @Column(name = "CALL_EXTERNAL_REFERENCE")
    private String externalReference;

    @JsonProperty("payment_type")
    @Column(name = "CALL_PAYMENT_TYPE")
    private String paymentType;

    @JsonProperty("merchant_order_id")
    @Column(name = "CALL_MERCHAND_ORDER_ID")
    private String merchantOrderId;

    @JsonProperty("preference_id")
    @Column(name = "CALL_PREFERENCE_ID")
    private String preferenceId;

    @OneToOne
    private Checkout checkout;

}
