package com.srpinfotec.cvslog.controller;

import com.srpinfotec.cvslog.dto.ResponseDto;
import com.srpinfotec.cvslog.dto.request.CommitRqCond;
import com.srpinfotec.cvslog.dto.response.CommitRsDto;
import com.srpinfotec.cvslog.dto.response.FetchRsDto;
import com.srpinfotec.cvslog.dto.response.ProjectRsDto;
import com.srpinfotec.cvslog.service.CVSService;
import com.srpinfotec.cvslog.service.CommitService;
import com.srpinfotec.cvslog.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommitLogController {
    private final CVSService cvsService;
    private final ProjectService projectService;
    private final CommitService commitService;

    /**
     * fetch commit 요청
     */
    @PostMapping("/commit/fetch")
    public ResponseEntity<ResponseDto> fetchCommit(){
        FetchRsDto fetchRsDto = cvsService.fetchCvsHistory();

        return ResponseEntity
                .ok(ResponseDto.success(fetchRsDto));
    }

    /**
     * 관리 중인 모든 project 조회
     */
    @GetMapping("/project")
    public ResponseEntity<ResponseDto> projectList(){
        List<ProjectRsDto> projects = projectService.getAllProject();

        return ResponseEntity
                .ok(ResponseDto.success(projects));
    }

    @GetMapping("/commit")
    public ResponseEntity<ResponseDto> commitListByProject(
            @ModelAttribute CommitRqCond cond
    ){
        List<CommitRsDto> commits = commitService.getCommitList(cond);

        return ResponseEntity
                .ok(ResponseDto.success(commits));
    }
}
