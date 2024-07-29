package com.srpinfotec.cvslog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CVS_COMMIT_HISTORY")
public class Commit extends BaseTime{
    @Id @GeneratedValue
    @Column(name = "COMMIT_ID")
    private Long id;

    @Column(name = "COMMIT_MSG")
    private String commitMsg;

    @Column(name = "COMMIT_TIME")
    private LocalDateTime commitTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @OneToMany(mappedBy = "commit")
    private List<Revision> revisions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
}
