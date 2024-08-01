package com.srpinfotec.cvslog.service;

import com.srpinfotec.cvslog.dto.request.CommitRqCond;
import com.srpinfotec.cvslog.dto.response.CommitRsDto;
import com.srpinfotec.cvslog.repository.CommitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommitService {
    private final CommitRepository commitRepository;

    public List<CommitRsDto> getCommitList(CommitRqCond cond){
        return commitRepository.findCommitDto(cond);
    }
}
