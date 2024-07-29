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
@Table(name = "CVS_FILES",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"PROJECT_ID", "FILE_NAME", "FILE_PATH"})
        }
)
public class File extends BaseTime{
    @Id @GeneratedValue
    @Column(name = "FILE_ID")
    private Long id;

    @Column(name = "FILE_NAME")
    private String name;

    @Column(name = "FILE_PATH")
    private String path;

    @OneToMany(mappedBy = "file", fetch = FetchType.LAZY)
    private List<Revision> revisions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJECT_ID")
    private Project project;
}
