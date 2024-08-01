package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.dto.request.CommitRqCond;
import com.srpinfotec.cvslog.dto.response.CommitRsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class CommitRepositoryImplTest {
    @Autowired
    CommitRepository commitRepository;

    @Test
    public void 쿼리dsl조회테스트(){
        List<CommitRsDto> dtos = commitRepository.findCommitDtoWithoutRevision();

        for(CommitRsDto dto : dtos){
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void 쿼리ds조회with중첩구조1(){
        List<CommitRsDto> dtos = commitRepository.findCommitDto(new CommitRqCond(null, null));

        for(CommitRsDto dto : dtos){
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void 쿼리ds조회with중첩구조2(){
        List<CommitRsDto> dtos = commitRepository.findCommitDto(new CommitRqCond(null, "David"));

        for(CommitRsDto dto : dtos){
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void 쿼리ds조회with중첩구조3(){
        List<CommitRsDto> dtos = commitRepository.findCommitDto(new CommitRqCond("HypertexErp", null));

        for(CommitRsDto dto : dtos){
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void 쿼리ds조회with중첩구조4(){
        List<CommitRsDto> dtos = commitRepository.findCommitDto(new CommitRqCond("HypertexErp", "David"));

        for(CommitRsDto dto : dtos){
            System.out.println("dto = " + dto);
        }
    }
}