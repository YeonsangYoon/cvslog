package com.srpinfotec.cvslog;

import com.srpinfotec.cvslog.common.CVSProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({CVSProperties.class})
public class CvslogApplication {
    public static void main(String[] args) {
        SpringApplication.run(CvslogApplication.class, args);
    }
}
