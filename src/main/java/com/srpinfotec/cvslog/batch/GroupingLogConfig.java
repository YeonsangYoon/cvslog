package com.srpinfotec.cvslog.batch;

import com.srpinfotec.cvslog.domain.Commit;
import com.srpinfotec.cvslog.domain.Project;
import com.srpinfotec.cvslog.domain.User;
import com.srpinfotec.cvslog.dto.LogEntryGroup;
import com.srpinfotec.cvslog.repository.CommitRepository;
import com.srpinfotec.cvslog.repository.ProjectRepository;
import com.srpinfotec.cvslog.repository.RevisionLogRepository;
import com.srpinfotec.cvslog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GroupingLogConfig {
    private final RevisionLogRepository revisionLogRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommitRepository commitRepository;

    @Bean
    @JobScope
    public Step GroupingLogStep(JobRepository jr, PlatformTransactionManager ptm,
                                ItemReader<LogEntryGroup> logGroupItemReader,
                                ItemProcessor<LogEntryGroup, Commit> logGroupItemProcessor,
                                ItemWriter<Commit> logGroupItemWriter
                                ){
        return new StepBuilder("GroupingLogStep", jr)
                .<LogEntryGroup, Commit>chunk(5, ptm)
                .reader(logGroupItemReader)
                .processor(logGroupItemProcessor)
                .writer(logGroupItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<LogEntryGroup> logGroupItemReader(){
        return new RepositoryItemReaderBuilder<LogEntryGroup>()
                .name("LogGroupItemReader")
                .repository(revisionLogRepository)
                .methodName("findLogGroup")
                .arguments()
                .pageSize(5)
                .sorts(Collections.singletonMap("date", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<LogEntryGroup, Commit> logGroupItemProcessor() {
        return new ItemProcessor<LogEntryGroup, Commit>() {
            @Override
            public Commit process(LogEntryGroup item) throws Exception {
                Project project = projectRepository.findByNaturalId(item.getProjectName())
                        .orElseGet(() -> projectRepository.save(new Project(item.getProjectName())));

                User user = userRepository.findByNaturalId(item.getUsername())
                        .orElseGet(() -> userRepository.save(new User(item.getUsername())));

                return new Commit(item.getDate(), project, user);
            }
        };
    }

    @StepScope
    @Bean
    public ItemWriter<Commit> logGroupItemWriter() {
        return new ItemWriter<Commit>() {
            @Override
            public void write(Chunk<? extends Commit> chunk) throws Exception {
                chunk.forEach(commitRepository::save);
            }
        };
    }
}
