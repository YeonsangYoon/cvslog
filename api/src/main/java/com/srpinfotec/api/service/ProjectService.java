package com.srpinfotec.api.service;

import com.srpinfotec.api.dto.response.ProjectRsDto;
import com.srpinfotec.core.repository.ProjectRepository;
import com.srpinfotec.api.repository.CommitQueryRepository;
import com.srpinfotec.api.repository.ProjectQueryRepository;
import com.srpinfotec.core.entity.Commit;
import com.srpinfotec.core.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectQueryRepository projectQueryRepository;
    private final CommitQueryRepository commitQueryRepository;
    private final ProjectRepository projectRepository;

    public List<ProjectRsDto> getAllProject(){
        List<Project> projects = projectRepository.findOrderByName();

        return projects.stream().map(p -> new ProjectRsDto(p.getName()))
                .toList();
    }

    public List<ProjectRsDto> getAllProjectWithTodayCommit(){
        List<ProjectRsDto> allDtos = projectQueryRepository.findAllDtos();
        List<Commit> todayCommit = commitQueryRepository.findTodayCommit();

        Map<Long, Integer> projectTodayCommitCount = new HashMap<>();

        todayCommit.forEach(commit -> {
            Long proejctId = commit.getProject().getId();
            projectTodayCommitCount.put(proejctId, projectTodayCommitCount.getOrDefault(proejctId, 0) + 1);
        });

        allDtos.forEach(projectRsDto -> {
            int count = projectTodayCommitCount.getOrDefault(projectRsDto.getProjectId(), 0);

            if(count > 0){
                projectRsDto.addCommitCountToName(count);
            }
        });

        return allDtos;
    }
}
