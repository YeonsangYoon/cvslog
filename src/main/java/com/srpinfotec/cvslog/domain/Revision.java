package com.srpinfotec.cvslog.domain;

import com.srpinfotec.cvslog.dto.RevisionLogEntry;
import com.srpinfotec.cvslog.dto.response.RevisionRsDto;
import com.srpinfotec.cvslog.error.CustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CVS_FILE_REVISION",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"FILE_ID", "VERSION"})
    }
)
public class Revision extends BaseTime{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVISION_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "REVISION_TYPE")
    private RevisionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMIT_ID")
    private Commit commit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_ID", nullable = false)
    private File file;

    @Column(name = "VERSION")
    private Long version;

    public Revision(RevisionType type, File file, Long version) {
        this.type = type;
        this.file = file;
        this.version = version;
    }

    public Revision(RevisionType type, Commit commit, File file, Long version) {
        this.type = type;
        this.commit = commit;
        this.file = file;
        this.version = version;
    }

    public void setCommit(Commit commit){
        if(this.commit != null && this.commit != commit) {
            throw new CustomException("Revision의 Commit은 변경 불가");
        } else if(commit == null){
            throw new CustomException("Commit이 null");
        }

        this.commit = commit;
        this.commit.getRevisions().add(this);
    }

    public RevisionRsDto toRsDto(){
        return new RevisionRsDto(this.getType(), this.getVersion(), this.getFile().getName(), this.getFile().getPath());
    }
}
