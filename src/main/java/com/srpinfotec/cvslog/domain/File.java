package com.srpinfotec.cvslog.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CVS_FILES")
public class File extends BaseTime{
    @Id @GeneratedValue
    private Long id;

    private String filename;

    private String path;

    private String lastRevision;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    private List<CommitFile> commits = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;
}
