package com.teamteach.commons.api;

import io.swagger.annotations.Api;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class ApiResponse<T> {
    boolean status;
    int code;
    String msg;
    T data;

    public static ApiResponse createSuccessResponse(Object data) {
        return ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .status(true)
                .data(data)
                .build();
    }

    public static ApiResponse createFailedResponse(int code, Object data) {
        return ApiResponse.builder()
                .code(code)
                .status(false)
                .data(data)
                .build();
    }
}
