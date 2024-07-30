package com.srpinfotec.cvslog;

import com.srpinfotec.cvslog.common.CVSProperties;
import com.srpinfotec.cvslog.common.ShellCommand;
import com.srpinfotec.cvslog.service.FileService;
import com.srpinfotec.cvslog.service.ProjectService;
import com.srpinfotec.cvslog.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({CVSProperties.class, ShellCommand.class})
public class CvslogApplication {
    public static void main(String[] args) {
        SpringApplication.run(CvslogApplication.class, args);
    }
}
