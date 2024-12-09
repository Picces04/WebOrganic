package com.projectweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "OGN_SALEPRICE")
public class OgnSaleprice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SALEID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "PRODUCTID")
    private OgnProduct productid;

    @Column(name = "SALEPERCENTAGE", nullable = false, precision = 5, scale = 2)
    private BigDecimal salepercentage;

    @Column(name = "STARTDATE", nullable = false)
    private LocalDate startdate;

    @Column(name = "ENDDATE", nullable = false)
    private LocalDate enddate;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "CATEGORYID")
    private OgnCategory categoryid;

}