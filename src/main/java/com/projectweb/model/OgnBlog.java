package com.projectweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "OGN_BLOG")
public class OgnBlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "BLOGNAME", nullable = false)
    private String blogname;

    @Column(name = "IMAGE")
    private String image;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "BLOGDATE")
    private Instant blogdate;

    @Column(name = "BLOGSHORTDESC")
    private String blogshortdesc;

    @Lob
    @Column(name = "BLOGDESC")
    private String blogdesc;

    @Column(name = "TAG")
    private String tag;

}