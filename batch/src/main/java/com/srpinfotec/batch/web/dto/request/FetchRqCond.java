package com.srpinfotec.batch.web.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FetchRqCond {
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate baseDate;
    private String password;
    private Long chuckSize;

    public FetchRqCond(LocalDate baseDate, String password, Long chuckSize) {
        this.baseDate = baseDate;
        this.password = password;
        this.chuckSize = chuckSize;
    }
}