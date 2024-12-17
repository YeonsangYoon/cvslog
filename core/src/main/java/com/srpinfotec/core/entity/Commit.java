package com.srpinfotec.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CVS_COMMIT_HISTORY",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"PROJECT_ID", "USER_ID", "COMMIT_TIME"})
        }
)
public class Commit extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMIT_ID")
    private Long id;

    @Setter
    @Column(name = "COMMIT_MSG")
    private String commitMsg;

    @Column(name = "COMMIT_TIME", nullable = false)
    private LocalDateTime commitTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @OneToMany(mappedBy = "commit")
    private List<Revision> revisions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;


    public Commit(LocalDateTime commitTime, Project project, User user) {
        this.commitTime = commitTime;
        this.project = project;
        this.user = user;
    }
}
