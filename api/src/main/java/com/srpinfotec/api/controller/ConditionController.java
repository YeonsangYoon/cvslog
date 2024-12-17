package com.srpinfotec.api.controller;

import com.srpinfotec.api.dto.ResponseDto;
import com.srpinfotec.api.dto.response.ProjectRsDto;
import com.srpinfotec.api.dto.response.UserRsDto;
import com.srpinfotec.api.service.ProjectService;
import com.srpinfotec.api.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConditionController {
    private final ProjectService projectService;
    private final UserService userService;

    /**
     * 관리 중인 모든 project 조회
     */
    @GetMapping("/project")
    public ResponseEntity<ResponseDto> projectList(){
        List<ProjectRsDto> projects = projectService.getAllProject();

        return ResponseEntity
                .ok(ResponseDto.success(projects));
    }

    /**
     * 모든 Cvs User 조회
     */
    @GetMapping("/user")
    public ResponseEntity<ResponseDto> userList(){
        List<UserRsDto> users = userService.getAllUser();

        return ResponseEntity
                .ok(ResponseDto.success(users));
    }

    /**
     * 화면에서 사용할 검색 조건 (Project, User 한번에)
     */
    @GetMapping("/condition")
    public ResponseEntity<ResponseDto> searchCondition(){
        SearchConditionRsDto dto = new SearchConditionRsDto(
                projectService.getAllProjectWithTodayCommit(),
                userService.getAllUser()
        );

        return ResponseEntity
                .ok(ResponseDto.success(dto));
    }

    @Data
    @AllArgsConstructor
    public static class SearchConditionRsDto{
        private List<ProjectRsDto> project;
        private List<UserRsDto> user;
    }
}
