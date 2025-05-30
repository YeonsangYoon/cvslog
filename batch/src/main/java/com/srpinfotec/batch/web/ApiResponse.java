package com.srpinfotec.batch.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private String errorMsg;
    private T data;

    public ApiResponse(String status, String errorMsg, T data) {
        this.status = status;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public static <T>ApiResponse<T> success(T data){
        return new ApiResponse<T>("success", null, data);
    }

    public static <T>ApiResponse<T> error(String errorMsg){
        return new ApiResponse<T>("error", errorMsg, null);
    }
}
