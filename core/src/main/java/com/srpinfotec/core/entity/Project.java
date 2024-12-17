package com.srpinfotec.core.entity;

import com.srpinfotec.core.value.UseType;
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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    private Long id;

    @NaturalId
    @Column(name = "PROJECT_NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "USE_YN")
    @Enumerated(EnumType.STRING)
    private UseType isUse;

    @OneToMany(mappedBy = "project")
    private List<Commit> commits = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    private List<File> files = new ArrayList<>();

    public Project(String name) {
        this.name = name;
    }

    @PrePersist
    public void prePersist(){
        if(this.isUse == null) this.isUse = UseType.USE;
    }
}
