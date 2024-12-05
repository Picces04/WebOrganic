package com.projectweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "OGN_CART")
public class OgnCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARTID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "USERID")
    private com.projectweb.model.OgnUser userid;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "PRODUCTID")
    private com.projectweb.model.OgnProduct productid;

    @Column(name = "QUANTITY", nullable = false)
    private Long quantity;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CREATEDDATE")
    private Instant createddate;

}