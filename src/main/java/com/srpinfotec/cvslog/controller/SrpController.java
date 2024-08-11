package com.srpinfotec.cvslog.controller;

import com.srpinfotec.cvslog.dto.ResponseDto;
import com.srpinfotec.cvslog.dto.response.ProjectRsDto;
import com.srpinfotec.cvslog.dto.response.UserRsDto;
import com.srpinfotec.cvslog.service.ProjectService;
import com.srpinfotec.cvslog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SrpController {
    private final ProjectService projectService;
    private final UserService userService;

    /**
     * 화면에서 사용할 검색 조건
     */
    @GetMapping("/condition")
    public ResponseEntity<ResponseDto> searchCondition(){
        SearchConditionRsDto dto = new SearchConditionRsDto(
                projectService.getAllProject(),
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
