package com.srpinfotec.cvslog.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "COMMIT_HISTORY")
public class Commit extends BaseTime{
    @Id @GeneratedValue
    @Column(name = "COMMIT_ID")
    private Long id;

    private String commitMsg;

    private LocalDateTime commitTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

}
