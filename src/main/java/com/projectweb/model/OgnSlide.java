package com.projectweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "OGN_SLIDE")
public class OgnSlide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SLIDEID", nullable = false)
    private Long id;

    @Nationalized
    @Column(name = "IMAGEURL", nullable = false)
    private String imageurl;

    @Column(name = "DESCR")
    private String descr;

    @Column(name = "STATUS", length = 20)
    private String status;

}