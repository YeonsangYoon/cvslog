package com.srpinfotec.cvslog.batch;

import com.srpinfotec.cvslog.domain.RevisionLog;
import com.srpinfotec.cvslog.domain.RevisionType;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Revision Log를 엔티티로 변환해주는 Mapper 클래스
 */
public class LogToEntityMapper implements FieldSetMapper<RevisionLog> {
    @Override
    public RevisionLog mapFieldSet(FieldSet fieldSet) throws BindException {
        System.out.println(Arrays.toString(fieldSet.getValues()));
        LocalDateTime date = LocalDateTime.parse(
                fieldSet.readString(1) + " " + fieldSet.readString(2),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );

        String[] path = fieldSet.readString(7).split("/");

        return new RevisionLog(
                RevisionType.getType(fieldSet.readString(0)),
                fieldSet.readString(6),
                fieldSet.readString(7),
                fieldSet.readString(4),
                Long.parseLong(fieldSet.readString(5).substring(2)),
                date,
                path[0]
        );
    }
}
