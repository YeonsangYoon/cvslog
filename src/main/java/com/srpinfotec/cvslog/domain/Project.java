package com.srpinfotec.cvslog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CVS_REPOSITORY")
public class Project extends BaseTime {
    @Id @GeneratedValue
    @Column(name = "PROJECT_ID")
    private Long id;

    @Column(name = "PROJECT_NAME")
    private String name;

    @Column(name = "PROJECT_PATH")
    private String path;

    @OneToMany(mappedBy = "project")
    private List<Commit> commits = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<File> files = new ArrayList<>();

    public Project(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public Project(String name) {
        this.name = name;
    }
}
