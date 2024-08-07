package com.srpinfotec.cvslog.service;

import com.srpinfotec.cvslog.domain.Revision;
import com.srpinfotec.cvslog.dto.response.RevisionRsDto;
import com.srpinfotec.cvslog.repository.RevisionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RevisionService {
    private final RevisionRepository revisionRepository;

    public List<RevisionRsDto> getRevisionByCommit(Long commitId){
        List<Revision> revisions =  revisionRepository.findByCommitId(commitId);
        return revisions.stream().map(Revision::toRsDto).toList();
    }
}
