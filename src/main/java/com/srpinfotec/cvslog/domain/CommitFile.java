package com.srpinfotec.cvslog.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "CVS_COMMIT_FILE")
public class CommitFile extends BaseTime{
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMIT_ID")
    private Commit commit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_ID")
    private File file;

    private String revision;
}
