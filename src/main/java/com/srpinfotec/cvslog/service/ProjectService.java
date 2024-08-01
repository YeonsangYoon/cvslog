package com.srpinfotec.cvslog.service;

import com.srpinfotec.cvslog.domain.Project;
import com.srpinfotec.cvslog.dto.response.ProjectRsDto;
import com.srpinfotec.cvslog.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public List<ProjectRsDto> getAllProject(){
        List<Project> projects = projectRepository.findAll();

        return projects.stream().map(p -> new ProjectRsDto(p.getName()))
                .toList();
    }
}
