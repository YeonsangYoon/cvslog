package com.srpinfotec.batch.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {
    private String status;
    private String errorMsg;
    private Object data;

    public ApiResponse(String status, String errorMsg, Object data) {
        this.status = status;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public static ApiResponse success(Object data){
        return new ApiResponse("success", null, data);
    }

    public static ApiResponse error(String errorMsg){
        return new ApiResponse("error", errorMsg, null);
    }
}
