package com.projectweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "OGN_USER")
public class OgnUser {
    @Id
    @Column(name = "USERID", nullable = false, length = 36)
    private String userid;

    @Nationalized
    @Column(name = "USERNAME", nullable = false, length = 50)
    private String username;

    @Nationalized
    @Column(name = "PASSWORDHASH", nullable = false)
    private String passwordhash;

    @Nationalized
    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CREATEDDATE")
    private Instant createddate;

    @OneToMany(mappedBy = "userid")
    private Set<OgnAdmin> ognAdmins = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userid")
    private Set<OgnCart> ognCarts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userid")
    private Set<OgnOrder> ognOrders = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userid")
    private Set<OgnStarrating> ognStarratings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userid")
    private Set<com.projectweb.model.OgnWishlist> ognWishlists = new LinkedHashSet<>();

    @Column(name = "ROLE")
    private String role;

    @OneToMany(mappedBy = "userid")
    private Set<OgnBillingDetail> ognBillingDetails = new LinkedHashSet<>();

    @Column(name = "PHONE", length = 15)
    private String phone;

}