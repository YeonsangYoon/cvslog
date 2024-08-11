package com.srpinfotec.cvslog.batch.mapper;

import com.srpinfotec.cvslog.batch.dto.RevisionLogEntry;
import com.srpinfotec.cvslog.domain.RevisionType;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Revision Log를 엔티티로 변환해주는 Mapper 클래스
 */
public class LogToEntityMapper implements FieldSetMapper<RevisionLogEntry> {
    private static final Integer EXPECTED_FIELD_COUNT = 10;

    @Override
    public RevisionLogEntry mapFieldSet(FieldSet fieldSet) throws BindException {
        if(fieldSet.getFieldCount() < EXPECTED_FIELD_COUNT){
            throw new FlatFileParseException("로그 형식 오류", Arrays.toString(fieldSet.getValues()));
        }

        int extraTokenCount = fieldSet.getFieldCount() - EXPECTED_FIELD_COUNT;  // 파일 이름 중간에 공백 있는 경우

        try{
            // Type
            RevisionType type = RevisionType.getType(fieldSet.readString(0));

            // Date
            LocalDateTime date = LocalDateTime.parse(
                    fieldSet.readString(1) + " " + fieldSet.readString(2),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );

            // username
            String username = fieldSet.readString(4);

            // version
            Long version = Long.parseLong(fieldSet.readString(5).substring(2));

            // filename
            StringBuilder filename = new StringBuilder();
            for(int i = 0; i <= extraTokenCount; i++){
                filename.append(fieldSet.readString(6 + i));
            }

            // filepath
            String filepath = fieldSet.readString(7 + extraTokenCount);

            // projectName
            String projectName = filepath.split("/")[0];

            return new RevisionLogEntry(
                    type,
                    filename.toString(),
                    filepath,
                    username,
                    version,
                    date,
                    projectName
            );
        } catch (Exception e){
            throw new FlatFileParseException("로그 형식 오류", Arrays.toString(fieldSet.getValues()));
        }
    }
}
