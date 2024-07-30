package com.srpinfotec.cvslog.service;

import com.srpinfotec.cvslog.common.CustomException;
import com.srpinfotec.cvslog.domain.Commit;
import com.srpinfotec.cvslog.domain.Project;
import com.srpinfotec.cvslog.domain.Revision;
import com.srpinfotec.cvslog.domain.User;
import com.srpinfotec.cvslog.dto.LogEntryGroup;
import com.srpinfotec.cvslog.dto.RevisionLogEntry;
import com.srpinfotec.cvslog.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CVSService {
    private final RevisionRepository revisionRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CommitRepository commitRepository;
    private final FileRepository fileRepository;

    @PostConstruct
    private void initTestLog(){
        updateHistory(getTestLogs());
    }

    private List<String> getTestLogs(){
        ClassPathResource commitLog = new ClassPathResource("commitLog.txt");

        try{
            File commitFile = commitLog.getFile();

            return Files.readAllLines(Paths.get(commitFile.getPath()));

        } catch (IOException e){
            throw new CustomException("파일 읽기 실패");
        }
    }

    @Transactional
    public void updateHistory(Iterable<String> logs) {
        List<RevisionLogEntry> newEntries = new ArrayList<>();

        for (String log : logs) {
            RevisionLogEntry entry = RevisionLogEntry.parseLog(log);

            Optional<Revision> byLog = revisionRepository.findByLog(entry.getFilename(), entry.getFilepath(), entry.getVersion());

            if (byLog.isEmpty()) {
                newEntries.add(entry);
            }
        }

        Map<LogEntryGroup, List<RevisionLogEntry>> commitMap = newEntries.stream().collect(Collectors.groupingBy(e ->
                new LogEntryGroup(e.getDate(), e.getProjectName(), e.getUsername())));

        // 커밋 단위 저장
        for(Map.Entry<LogEntryGroup, List<RevisionLogEntry>> commitGroup : commitMap.entrySet()){
            String username = commitGroup.getKey().getUsername();
            String projectName = commitGroup.getKey().getProjectName();
            LocalDateTime commitDate = commitGroup.getKey().getDate();

            User user = userRepository.findByNaturalId(username)
                    .orElseGet(() -> userRepository.save(new User(username)));

            Project project = projectRepository.findByNaturalId(projectName)
                    .orElseGet(() -> projectRepository.save(new Project(projectName)));


            Commit commit = commitRepository.save(new Commit(commitDate, project, user));

            // Revision 단위 저장
            for(RevisionLogEntry reviLog : commitGroup.getValue()){

                Revision revision = new Revision(
                        reviLog.getType(),
                        commit,
                        fileRepository.findByLog(reviLog.getFilename(), reviLog.getFilepath(), project.getId())
                                .orElseGet(() -> fileRepository.save(
                                        new com.srpinfotec.cvslog.domain.File(reviLog.getFilename(), reviLog.getFilepath(), project))
                                ),
                        reviLog.getVersion());

                revisionRepository.save(revision);
            }
        }
    }
}
