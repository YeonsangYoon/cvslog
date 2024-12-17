package com.srpinfotec.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    private String status;
    private String errorMsg;
    private Object data;

    public ResponseDto(String status, String errorMsg, Object data) {
        this.status = status;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public static ResponseDto success(Object data){
        return new ResponseDto("success", null, data);
    }

    public static ResponseDto error(String errorMsg){
        return new ResponseDto("error", errorMsg, null);
    }
}
