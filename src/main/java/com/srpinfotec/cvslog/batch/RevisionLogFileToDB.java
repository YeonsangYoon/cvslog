package com.srpinfotec.cvslog.batch;

import com.srpinfotec.cvslog.batch.dto.RevisionLogEntry;
import com.srpinfotec.cvslog.batch.mapper.LogToEntityMapper;
import com.srpinfotec.cvslog.common.CVSProperties;
import com.srpinfotec.cvslog.domain.*;
import com.srpinfotec.cvslog.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RevisionLogFileToDB {
    private final CommitRepository commitRepository;
    private final RevisionRepository revisionRepository;
    private final FileRepository fileRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final CVSProperties cvsProperties;

    private final EntityManager entityManager;

    @Bean
    @JobScope
    public Step revisionFileToDBStep(JobRepository jr,
                                     PlatformTransactionManager ptm,
                                     ItemReader<RevisionLogEntry> revisionLogItemReader,
                                     ItemProcessor<RevisionLogEntry, Revision> revisionLogItemProcessor,
                                     ItemWriter<Revision> revisionItemWriter,
                                     SkipPolicy parseLogSkipPolicy,
                                     @Value("#{jobParameters['chuckSize']}") Long chuckSize
    ){
        return new StepBuilder("RevisionFileToDBStep", jr)
                .<RevisionLogEntry, Revision>chunk(chuckSize.intValue(), ptm)
                .reader(revisionLogItemReader)
                .processor(revisionLogItemProcessor)
                .writer(revisionItemWriter)
                .faultTolerant()
                .skipPolicy(parseLogSkipPolicy)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<RevisionLogEntry> revisionLogItemReader(){
        return new FlatFileItemReaderBuilder<RevisionLogEntry>()
                .name("RevisionLogItemReader")
                .resource(new FileSystemResource(cvsProperties.getLogFilePath()))
                .lineTokenizer(new LineTokenizer() {
                    @Override
                    public FieldSet tokenize(String line) {
                        if (line == null || line.trim().isEmpty()) {
                            return new DefaultFieldSet(new String[0]);
                        }

                        String[] tokens = line.trim().split("\\s+");

                        return new DefaultFieldSet(tokens);
                    }
                })
                .fieldSetMapper(new LogToEntityMapper())
                .build();
    }

    @Bean
    @StepScope
    public CompositeItemProcessor<RevisionLogEntry, Revision> revisionLogItemProcessor(){
        CompositeItemProcessor<RevisionLogEntry, Revision> compositeItemProcessor = new CompositeItemProcessor<>();

        compositeItemProcessor.setDelegates(Arrays.asList(
                duplicationCheckItemProcessor(),
                dtoToEntityItemProcessor()
        ));

        return compositeItemProcessor;
    }

    @Bean
    @StepScope
    public ItemProcessor<RevisionLogEntry, RevisionLogEntry> duplicationCheckItemProcessor(){
        return new ItemProcessor<RevisionLogEntry, RevisionLogEntry>() {
            @Override
            public RevisionLogEntry process(RevisionLogEntry logEntry) throws Exception {
                if(logEntry == null) return null;

                Optional<Revision> byLog = revisionRepository.findByLog(logEntry.getFilename(), logEntry.getFilepath(), logEntry.getVersion());

                return byLog.isEmpty() ? logEntry : null;
            }
        };
    }

    @Bean
    @StepScope
    public ItemProcessor<RevisionLogEntry, Revision> dtoToEntityItemProcessor(){
        return new ItemProcessor<RevisionLogEntry, Revision>() {
            @Override
            public Revision process(RevisionLogEntry logEntry) throws Exception {
                Project project = projectRepository.findByNaturalId(logEntry.getProjectName())
                        .orElseGet(() -> new Project(logEntry.getProjectName()));

                User user = userRepository.findByNaturalId(logEntry.getUsername())
                        .orElseGet(() -> new User(logEntry.getUsername()));

                File file = fileRepository.findByLog(logEntry.getFilename(), logEntry.getFilepath(), logEntry.getProjectName())
                        .orElseGet(() -> new File(logEntry.getFilename(), logEntry.getFilepath(), project));

                Commit commit = commitRepository.findByRevision(logEntry.getDate(), logEntry.getProjectName(), logEntry.getUsername())
                        .orElseGet(() -> new Commit(logEntry.getDate(), project, user));

                entityManager.persist(project);
                entityManager.persist(user);
                entityManager.persist(file);
                entityManager.persist(commit);

                return new Revision(
                        logEntry.getType(),
                        commit,
                        file,
                        logEntry.getVersion()
                );
            }
        };
    }

    @Bean
    @StepScope
    public ItemWriter<Revision> revisionLogItemWriter(){
        return new ItemWriter<Revision>() {
            @Override
            public void write(Chunk<? extends Revision> chunk) throws Exception {
                chunk.forEach(revisionRepository::save);
                log.debug("Write revision log to database (revision count : {})", String.valueOf(chunk.size()));
            }
        };
    }

    @Bean
    public SkipPolicy parseLogSkipPolicy(){
        return new SkipPolicy() {
            @Override
            public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
                return t instanceof FlatFileParseException && skipCount <= 3;
            }
        };
    }
}
