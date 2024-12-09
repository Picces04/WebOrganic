package com.projectweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "OGN_ORDERS")
public class OgnOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDERID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "USERID")
    private com.projectweb.model.OgnUser userid;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ORDERDATE")
    private Instant orderdate;

    @Nationalized
    @Column(name = "STATUS", length = 50)
    private String status;

    @OneToMany(mappedBy = "orderid")
    private Set<OgnOrderdetail> ognOrderdetails = new LinkedHashSet<>();

    @OneToMany(mappedBy = "orderid")
    private Set<OgnBillingDetail> ognBillingDetails = new LinkedHashSet<>();

}