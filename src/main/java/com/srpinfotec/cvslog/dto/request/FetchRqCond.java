package com.srpinfotec.cvslog.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class FetchRqCond {
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate baseDate;
    private String password;
}
