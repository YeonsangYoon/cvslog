package com.srpinfotec.api.service;

import com.srpinfotec.api.dto.response.RevisionRsDto;
import com.srpinfotec.core.repository.RevisionRepository;
import com.srpinfotec.core.entity.Revision;
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

        return revisions.stream().map(revision -> RevisionRsDto.builder()
                .type(revision.getType())
                .version(revision.getVersion())
                .filename(revision.getFile().getName())
                .filepath(revision.getFile().getPath())
                .build()).toList();
    }
}
