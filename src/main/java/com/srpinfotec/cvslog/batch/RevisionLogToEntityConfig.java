package com.srpinfotec.cvslog.batch;

import com.srpinfotec.cvslog.common.CVSProperties;
import com.srpinfotec.cvslog.domain.Revision;
import com.srpinfotec.cvslog.domain.RevisionLog;
import com.srpinfotec.cvslog.repository.RevisionLogRepository;
import com.srpinfotec.cvslog.repository.RevisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class RevisionLogToEntityConfig {
    private final RevisionLogRepository revisionLogRepository;
    private final RevisionRepository revisionRepository;
    private final CVSProperties cvsProperties;

    @Bean
    @JobScope
    public Step revisionLogToEntityStep(JobRepository jr, PlatformTransactionManager ptm,
                                        ItemReader<RevisionLog> revisionLogItemReader,
                                        ItemWriter<RevisionLog> revisionLogItemWriter){
        return new StepBuilder("RevisionLogToEntityStep", jr)
                .<RevisionLog, RevisionLog>chunk(10, ptm)
                .reader(revisionLogItemReader)
                .writer(revisionLogItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<RevisionLog> revisionLogItemReader(){
        return new FlatFileItemReaderBuilder<RevisionLog>()
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
    public ItemWriter<RevisionLog> revisionLogItemWriter(){
        return new ItemWriter<RevisionLog>() {
            @Override
            public void write(Chunk<? extends RevisionLog> chunk) throws Exception {
                chunk.forEach(log -> {
                    Optional<Revision> byLog = revisionRepository.findByLog(log.getFilename(), log.getFilepath(), log.getVersion());

                    if(byLog.isEmpty()){
                        revisionLogRepository.save(log);
                    }
                });
            }
        };
    }
}