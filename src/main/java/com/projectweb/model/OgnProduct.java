package com.projectweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "OGN_PRODUCT")
public class OgnProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCTID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "CATEGORYID")
    private OgnCategory categoryid;

    @Nationalized
    @Column(name = "PRODUCTNAME", nullable = false, length = 100)
    private String productname;

    @Nationalized
    @Column(name = "SHORTDESC", length = 500)
    private String shortdesc;

    @Nationalized
    @Column(name = "DESCRIPTION", length = 1500)
    private String description;

    @Column(name = "PRICE", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;

    @Column(name = "STOCK", nullable = false)
    private Long stock;

    @Nationalized
    @Column(name = "UNIT", length = 50)
    private String unit;

    @Nationalized
    @Column(name = "IMAGE")
    private String image;

    @Lob
    @Column(name = "LISTIMAGE")
    private String listimage;

    @OneToMany(mappedBy = "productid")
    private Set<OgnCart> ognCarts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productid")
    private Set<OgnOrderdetail> ognOrderdetails = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productid")
    private Set<com.projectweb.model.OgnSaleprice> ognSaleprices = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productid")
    private Set<com.projectweb.model.OgnStarrating> ognStarratings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "productid")
    private Set<com.projectweb.model.OgnWishlist> ognWishlists = new LinkedHashSet<>();

}