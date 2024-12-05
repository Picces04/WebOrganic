package com.projectweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "OGN_STARRATING")
public class OgnStarrating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RATINGID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "PRODUCTID")
    private OgnProduct productid;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "USERID")
    private com.projectweb.model.OgnUser userid;

    @Column(name = "RATING")
    private Long rating;

    @Nationalized
    @Column(name = "REVIEW")
    private String review;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "RATINGDATE")
    private Instant ratingdate;

    @Nationalized
    @Column(name = "STATUS", length = 50)
    private String status;

}