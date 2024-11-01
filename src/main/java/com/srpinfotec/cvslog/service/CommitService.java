package com.srpinfotec.cvslog.service;

import com.srpinfotec.cvslog.dto.request.CommitRqCond;
import com.srpinfotec.cvslog.dto.response.CommitRsDto;
import com.srpinfotec.cvslog.repository.CommitRepository;
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
    private final CommitRepository commitRepository;

    public List<CommitRsDto> getCommitList(CommitRqCond cond){
        return (cond.getPage() == null) ?
                commitRepository.findCommitDto(cond) :
                commitRepository.findCommmitDtoByPage(cond);
    }

    public List<CommitRsDto> getCommitWithoutRevision(CommitRqCond cond){
        return (cond.getPage() == null) ?
                commitRepository.findCommitDtoWithoutRevision(cond) :
                commitRepository.findCommitDtoWithoutRevisionByPage(cond);
    }
}
