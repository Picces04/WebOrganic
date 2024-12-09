package com.projectweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "OGN_BILLING_DETAILS")
public class OgnBillingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BILLINGID", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Nationalized
    @Column(name = "STREETADDRESS", nullable = false)
    private String streetaddress;

    @Nationalized
    @Column(name = "CITY", nullable = false, length = 100)
    private String city;

    @Nationalized
    @Column(name = "PHONE", length = 20)
    private String phone;

    @Column(name = "DISTRICT", length = 100)
    private String district;

    @Column(name = "WARD", length = 100)
    private String ward;

    @Column(name = "SHIPPING_FEE")
    private Long shippingFee;

    @Column(name = "PAYMENT_METHOD", length = 50)
    private String paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "USERID")
    private OgnUser userid;

    @Column(name = "TOTALPRICE")
    private Float totalprice;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "ORDERID")
    private OgnOrder orderid;

}