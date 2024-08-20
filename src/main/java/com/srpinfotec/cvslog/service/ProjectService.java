package com.srpinfotec.cvslog.service;

import com.srpinfotec.cvslog.domain.Commit;
import com.srpinfotec.cvslog.domain.Project;
import com.srpinfotec.cvslog.dto.response.ProjectRsDto;
import com.srpinfotec.cvslog.repository.CommitRepository;
import com.srpinfotec.cvslog.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final CommitRepository commitRepository;

    public List<ProjectRsDto> getAllProject(){
        List<Project> projects = projectRepository.findOrderByName();

        return projects.stream().map(p -> new ProjectRsDto(p.getName()))
                .toList();
    }

    public List<ProjectRsDto> getAllProjectWithTodayCommit(){
        List<ProjectRsDto> allDtos = projectRepository.findAllDtos();
        List<Commit> todayCommit = commitRepository.findTodayCommit();

        Map<Long, Integer> projectTodayCommitCount
                = allDtos.stream().collect(Collectors.toMap(ProjectRsDto::getProjectId, projectRsDto -> 0));

        todayCommit.forEach(commit -> {
            Long proejctId = commit.getProject().getId();
            projectTodayCommitCount.put(proejctId, projectTodayCommitCount.get(proejctId) + 1);
        });

        allDtos.forEach(projectRsDto -> {
            int count = projectTodayCommitCount.get(projectRsDto.getProjectId());

            if(count > 0){
                projectRsDto.addCommitCountToName(count);
            }
        });

        return allDtos;
    }
}
