package com.srpinfotec.cvslog.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

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

    @NaturalId
    @Column(name = "PROJECT_NAME", unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "project")
    private List<Commit> commits = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<File> files = new ArrayList<>();

    public Project(String name) {
        this.name = name;
    }
}
