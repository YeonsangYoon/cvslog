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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMIT_ID")
    private Long id;

    @Column(name = "COMMIT_MSG")
    private String commitMsg;

    @Column(name = "COMMIT_TIME", nullable = false)
    private LocalDateTime commitTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID", nullable = false)
    private Project project;

    @OneToMany(mappedBy = "commit")
    private List<Revision> revisions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;


    public Commit(LocalDateTime commitTime, Project project, User user) {
        this.commitTime = commitTime;
        this.project = project;
        this.user = user;
    }

    public void setCommitMsg(String msg){
        this.commitMsg = msg;
    }
}
