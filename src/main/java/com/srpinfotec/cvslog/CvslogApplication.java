package com.srpinfotec.cvslog;

import com.srpinfotec.cvslog.service.FileService;
import com.srpinfotec.cvslog.service.ProjectService;
import com.srpinfotec.cvslog.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class CvslogApplication {
    private final UserService userService;
    private final ProjectService projectService;
    private final FileService fileService;

    @PostConstruct
    public void initCVS(){
        // 프로젝트

        // 파일

    }

    public static void main(String[] args) {
        SpringApplication.run(CvslogApplication.class, args);
    }
}
