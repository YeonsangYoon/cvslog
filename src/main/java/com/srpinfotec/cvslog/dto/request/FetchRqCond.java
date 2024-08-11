package com.srpinfotec.cvslog.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FetchRqCond {
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate baseDate;
    private String password;

    public FetchRqCond(LocalDate baseDate, String password) {
        this.baseDate = baseDate;
        this.password = password;
    }
}
