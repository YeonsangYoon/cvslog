package com.srpinfotec.cvslog.service;

import com.srpinfotec.cvslog.common.CustomException;
import com.srpinfotec.cvslog.domain.Revision;
import com.srpinfotec.cvslog.dto.LogEntryGroup;
import com.srpinfotec.cvslog.dto.RevisionLogEntry;
import com.srpinfotec.cvslog.repository.RevisionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

        RevisionLogEntry entry = null;
        for (String log : logs) {
            entry = RevisionLogEntry.parseLog(log);

            Optional<Revision> byLog = revisionRepository.findByLog(entry.getFilename(), entry.getFilepath(), entry.getVersion());

            if (byLog.isEmpty()) {
                newEntries.add(entry);
            }
        }

        Map<LogEntryGroup, List<RevisionLogEntry>> commitMap = newEntries.stream().collect(Collectors.groupingBy(e ->
                new LogEntryGroup(e.getDate(), e.getProjectName(), e.getUsername())));

        for(Map.Entry<LogEntryGroup, List<RevisionLogEntry>> e : commitMap.entrySet()){
            System.out.println("Commit = " + e.getKey() + "file count : " + e.getValue().size());
        }
    }
}
