package com.srpinfotec.cvslog.batch;

import com.srpinfotec.cvslog.batch.dto.RevisionLogEntry;
import com.srpinfotec.cvslog.batch.mapper.CvsLogUtil;
import com.srpinfotec.cvslog.batch.mapper.LogToEntityMapper;
import com.srpinfotec.cvslog.common.CVSProperties;
import com.srpinfotec.cvslog.common.command.CommandExecutor;
import com.srpinfotec.cvslog.domain.*;
import com.srpinfotec.cvslog.error.ShellCommandException;
import com.srpinfotec.cvslog.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
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
import org.springframework.util.StringUtils;

import java.util.*;

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

    private final CommandExecutor commandExecutor;

    @Bean
    @JobScope
    public Step revisionFileToDBStep(JobRepository jr,
                                     PlatformTransactionManager ptm,
                                     ItemReader<RevisionLogEntry> revisionLogItemReader,
                                     ItemProcessor<RevisionLogEntry, RevisionLogEntry> duplicationCheckItemProcessor,
                                     ItemProcessor<RevisionLogEntry, Revision> dtoToEntityItemProcessor,
                                     ItemProcessor<Revision, Revision> commitMessageItemProcessor,
                                     ItemWriter<Revision> revisionItemWriter,
                                     SkipPolicy parseLogSkipPolicy,
                                     @Value("#{jobParameters['chunkSize']}") Long chunkSize
    ){
        return new StepBuilder("RevisionFileToDBStep", jr)
                .<RevisionLogEntry, Revision>chunk(chunkSize.intValue(), ptm)
                .reader(revisionLogItemReader)
                .processor(new CompositeItemProcessor<>(
                        duplicationCheckItemProcessor,
                        dtoToEntityItemProcessor,
                        commitMessageItemProcessor
                ))
                .writer(revisionItemWriter)
                .faultTolerant()
                .skipPolicy(parseLogSkipPolicy)
                .build();
    }

    @Bean
    @JobScope
    public Step revisionFileToDBStepWithoutCommitMsg(JobRepository jr,
                                           PlatformTransactionManager ptm,
                                           ItemReader<RevisionLogEntry> revisionLogItemReader,
                                           ItemProcessor<RevisionLogEntry, RevisionLogEntry> duplicationCheckItemProcessor,
                                           ItemProcessor<RevisionLogEntry, Revision> dtoToEntityItemProcessor,
                                           ItemWriter<Revision> revisionItemWriter,
                                           SkipPolicy parseLogSkipPolicy,
                                           @Value("#{jobParameters['chunkSize']}") Long chuckSize
    ){
        return new StepBuilder("RevisionFileToDBStepWithoutCommitMsg", jr)
                .<RevisionLogEntry, Revision>chunk(chuckSize.intValue(), ptm)
                .reader(revisionLogItemReader)
                .processor(new CompositeItemProcessor<>(
                        duplicationCheckItemProcessor,
                        dtoToEntityItemProcessor
                ))
                .writer(revisionItemWriter)
                .faultTolerant()
                .skipPolicy(parseLogSkipPolicy)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<RevisionLogEntry> revisionLogItemReader(@Value("#{stepExecution}") StepExecution stepExecution){
        Long jobExecutionId = stepExecution.getJobExecutionId();

        return new FlatFileItemReaderBuilder<RevisionLogEntry>()
                .name("RevisionLogItemReader")
                .resource(new FileSystemResource(cvsProperties.getLogFilePath(jobExecutionId)))
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
    public ItemProcessor<Revision, Revision> commitMessageItemProcessor(){
        return new ItemProcessor<Revision, Revision>() {
            @Override
            public Revision process(Revision revision) throws Exception {
                Commit commit = revision.getCommit();

                // Commit Message 유무 확인
                if(StringUtils.hasLength(commit.getCommitMsg())){
                    return revision;
                }

                // file별 commit log 조회 command(cvs -d $CVSROOT rlog $FILE_PATH | iconv -f EUC-KR -t UTF-8)
                String command = "cvs -d " +
                        cvsProperties.getRoot() +
                        " rlog " +
                        revision.getFile().getPath() + "/" + revision.getFile().getName() +
                        " | iconv -f EUC-KR -t UTF-8";

                // sh Command 실행
                try{
                    List<String> logs = commandExecutor.executeWithOutput(command);
                    String commitMessage = CvsLogUtil.getCommitMessageFromLog(revision.getVersion(), logs.iterator());
                    commit.setCommitMsg(commitMessage);
                } catch (ShellCommandException ignored){}  // Message 로그 조회 실패 시 processor 넘어감

                return revision;
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
