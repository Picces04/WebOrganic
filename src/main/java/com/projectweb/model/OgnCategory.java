package com.projectweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "OGN_CATEGORY")
public class OgnCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORYID", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "CATEGORYNAME", nullable = false, length = 100)
    private String categoryname;

    @Nationalized
    @Column(name = "CATEGORYIMAGES", length = 2000)
    private String categoryimages;

    @Nationalized
    @Column(name = "STATUS", length = 50)
    private String status;

    @OneToMany(mappedBy = "categoryid")
    private Set<com.projectweb.model.OgnProduct> ognProducts = new LinkedHashSet<>();

}