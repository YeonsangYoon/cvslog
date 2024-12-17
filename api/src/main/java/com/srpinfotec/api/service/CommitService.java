package com.srpinfotec.api.service;

import com.srpinfotec.api.dto.request.CommitRqCond;
import com.srpinfotec.api.dto.response.CommitRsDto;
import com.srpinfotec.api.repository.CommitQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommitService {
    private final CommitQueryRepository commitQueryRepository;

    public List<CommitRsDto> getCommitList(CommitRqCond cond) {
        return commitQueryRepository.findCommitDto(cond);
    }

    public List<CommitRsDto> getCommitWithoutRevision(CommitRqCond cond) {
        return commitQueryRepository.findCommitDtoWithoutRevision(cond);
    }
}
