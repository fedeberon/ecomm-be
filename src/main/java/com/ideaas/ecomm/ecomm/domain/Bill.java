package com.ideaas.ecomm.ecomm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ideaas.ecomm.ecomm.domain.AFIP.Person;
import com.ideaas.ecomm.ecomm.enums.BillType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "BILLS")
public class Bill {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cuit")
    private String cuit;

    @Column(name = "codigoTipoComprobante")
    private String billType;

    @Column(name = "numeroPuntoVenta")
    private int pointNumber;

    @Column(name = "numeroComprobante")
    private Long number;

    @Column(name = "fechaEmision")
    private String date;

    @Column(name = "CAE")
    private String CAE;

    @Column(name = "fechaVencimientoCAE")
    private String dueDateCAE;

    @OneToOne
    @JoinColumn(name = "checkout_id")
    private Checkout checkout;

    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "creditCard")
    private String creditCard;

    @Column(name = "coupon")
    private String coupon;


    public Double getTotalAmount() {
        return checkout.getProducts().stream().mapToDouble(i -> i.getPrice()).sum();
    }

    public Bill(final BillBuilder builder) {
        this.cuit = builder.cuit;
        this.billType = builder.billType;
        this.pointNumber = builder.pointNumber;
        this.number = builder.number;
        this.date = builder.date;
        this.CAE = builder.CAE;
        this.dueDateCAE = builder.dueDateCAE;
        this.checkout = builder.checkout;
        this.user = builder.user;
        this.creditCard = builder.creditCard;
        this.coupon = builder.coupon;
        this.person = builder.person;
    }

    public BillType getBillTypeName() {
        return BillType.find(this.billType);
    }


    public static class BillBuilder {

        private String cuit;

        private String billType;

        private int pointNumber;

        private Long number;

        private String date;

        private String CAE;

        private String dueDateCAE;

        private Checkout checkout;

        private User user;

        private String creditCard;

        private String coupon;

        private Person person;


        public BillBuilder withCuit(final String cuit) {
            this.cuit = cuit;

            return this;
        }


        public BillBuilder withBillType(final String billType) {
            this.billType = billType;

            return this;
        }

        public BillBuilder withPointNumber(final int pointNumber) {
            this.pointNumber = pointNumber;

            return this;
        }

        public BillBuilder withNumber(final Long number) {
            this.number = number;

            return this;
        }

        public BillBuilder withDate(final String date) {
            this.date = date;

            return this;
        }

        public BillBuilder withCAE(final String CAE) {
            this.CAE = CAE;

            return this;
        }

        public BillBuilder withDueDateCAE(final String dueDateCAE) {
            this.dueDateCAE = dueDateCAE;

            return this;
        }

        public BillBuilder withCheckout(final Checkout checkout) {
            this.checkout = checkout;

            return this;
        }

        public BillBuilder withUser(final User user) {
            this.user = user;

            return this;
        }

        public BillBuilder withCreditCard(final String creditCard) {
            this.creditCard = creditCard;

            return this;
        }

        public BillBuilder withCoupon(final String coupon) {
            this.coupon = coupon;

            return this;
        }

        public BillBuilder withPerson(final Person person) {
            this.person = person;

            return this;
        }

        public Bill build(){
            return new Bill(this);
        }

    }
}