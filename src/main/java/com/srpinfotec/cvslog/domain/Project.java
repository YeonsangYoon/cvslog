package com.srpinfotec.cvslog.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CVS_REPOSITORY")
public class Project extends BaseTime {
    @Id @GeneratedValue
    private Long id;

    private String name;

    private String path;

    @OneToMany(mappedBy = "project")
    private List<Commit> commits = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<File> files = new ArrayList<>();
}
