package com.srpinfotec.cvslog.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FetchRsDto {
    private String fetchStatus;
    private Long fetchCount;            // revision 기준 새로운 데이터

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdated;

    public FetchRsDto(String fetchStatus, Long fetchCount, LocalDateTime lastUpdated) {
        this.fetchStatus = fetchStatus;
        this.fetchCount = fetchCount;
        this.lastUpdated = lastUpdated;
    }

    public static FetchRsDto nonExecutedJob(){
        return new FetchRsDto("NONE", 0L, null);
    }
}
