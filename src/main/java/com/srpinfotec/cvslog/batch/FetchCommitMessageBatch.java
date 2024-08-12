package com.srpinfotec.cvslog.batch;

import com.srpinfotec.cvslog.batch.mapper.CvsLogUtil;
import com.srpinfotec.cvslog.common.CVSProperties;
import com.srpinfotec.cvslog.common.command.CommandExecutor;
import com.srpinfotec.cvslog.domain.Commit;
import com.srpinfotec.cvslog.domain.Revision;
import com.srpinfotec.cvslog.error.ShellCommandException;
import com.srpinfotec.cvslog.repository.CommitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FetchCommitMessageBatch {
    private final CVSProperties cvsProperties;
    private final CommandExecutor commandExecutor;
    private final CommitRepository commitRepository;

    @Bean
    public Job fetchCommitMessageJob(JobRepository jr, PlatformTransactionManager ptm,
                                     Step fetchCommitMessageStep
    ){

        return new JobBuilder("FetchCommitMessageJob", jr)
                .incrementer(new RunIdIncrementer())
                .start(fetchCommitMessageStep)
                .build();
    }

    @Bean
    @JobScope
    public Step fetchCommitMessageStep(
            JobRepository jr,
            PlatformTransactionManager ptm,
            ItemReader<Commit> emptyMessageCommitReader,
            ItemProcessor<Commit, Commit> commitMessageProcessor,
            ItemWriter<Commit> commitMessageWriter,
            @Value("#{jobParameters['chunkSize']}") Long chunkSize
    ){

        return new StepBuilder("FetchCommitMessageStep", jr)
                .<Commit, Commit>chunk(chunkSize.intValue(), ptm)
                .reader(emptyMessageCommitReader)
                .processor(commitMessageProcessor)
                .writer(commitMessageWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Commit> emptyMessageCommitReader(@Value("#{jobParameters['chunkSize']}") Long chunkSize){
        return new RepositoryItemReaderBuilder<Commit>()
                .name("EmptyMessageCommitReader")
                .repository(commitRepository)
                .pageSize(chunkSize.intValue())
                .methodName("findCommitMsgIsNull")
                .arguments()
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Commit, Commit> commitMessageProcessor(){
        return new ItemProcessor<Commit, Commit>() {
            @Override
            public Commit process(Commit commit) throws Exception {
                // Commit Message 유무 확인
                if(StringUtils.hasLength(commit.getCommitMsg())){
                    return commit;
                }

                Revision revision = commit.getRevisions().get(0);

                String command = cvsProperties.getScriptDir() +
                        "/readlog.sh " +
                        revision.getFile().getPath() +
                        "/" +
                        revision.getFile().getName();

                List<String> logs;
                try{
                    logs = commandExecutor.executeWithOutput(command);
                } catch (ShellCommandException e){  // Message 로그 조회 실패 시 processor 넘어감
                    return commit;
                }

                // data 추출
                commit.setCommitMsg(CvsLogUtil.getCommitMessageFromLog(revision.getVersion(), logs.iterator()));

                return commit;
            }
        };
    }

    @Bean
    @StepScope
    public ItemWriter<Commit> commitMessageWriter(){
        return new ItemWriter<Commit>() {
            @Override
            public void write(Chunk<? extends Commit> commits) throws Exception {
                commits.forEach(commitRepository::save);
            }
        };
    }
}
