package com.srpinfotec.cvslog.repository;

import com.srpinfotec.cvslog.domain.Project;
import com.srpinfotec.cvslog.dto.request.CommitRqCond;
import com.srpinfotec.cvslog.dto.response.CommitRsDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
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

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    EntityManager em;

    @Test
    public void 쿼리dsl조회테스트(){
        List<CommitRsDto> dtos = commitRepository.findCommitDtoWithoutRevision(null);

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

    @Test
    public void 영속화_확인(){
        Project project = new Project("project");
        Project project1 = new Project("project1");

        em.persist(project);

        System.out.println("emf.getPersistenceUnitUtil().isLoaded(project) = " + emf.getPersistenceUnitUtil().isLoaded(project));
        System.out.println("emf.getPersistenceUnitUtil().isLoaded(project1) = " + emf.getPersistenceUnitUtil().isLoaded(project1));

        System.out.println("em.contains(project) = " + em.contains(project));
        System.out.println("em.contains(project1) = " + em.contains(project1));
    }
}