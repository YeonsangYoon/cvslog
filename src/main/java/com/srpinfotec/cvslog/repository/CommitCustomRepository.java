package com.srpinfotec.cvslog.repository;


import com.srpinfotec.cvslog.dto.request.CommitRqCond;
import com.srpinfotec.cvslog.dto.response.CommitRsDto;

import java.util.List;

public interface CommitCustomRepository {
    List<CommitRsDto> findCommitDtoWithoutRevision();

    List<CommitRsDto> findCommitDto(CommitRqCond cond);

    List<CommitRsDto> findCommmitDtoByPage(CommitRqCond cond);
}
