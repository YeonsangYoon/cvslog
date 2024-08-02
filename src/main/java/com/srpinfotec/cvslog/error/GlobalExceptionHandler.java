package com.srpinfotec.cvslog.error;

import com.srpinfotec.cvslog.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShellCommandException.class)
    public ResponseEntity<ResponseDto> handleShellCommandError(ShellCommandException e){
        log.error("Shell Command Error [{}] : {}", e.getCommand(), e.getMessage());

        return allException(e);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto> allException(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.error("내부 서버 오류"));
    }
}
