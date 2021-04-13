package com.teamteach.commons.api;

public class AbstractAppController {
    protected ApiResponse sendSuccessResponse(Object data) {
        return ApiResponse.createSuccessResponse(data);
    }
    protected ApiResponse sendStatusResponseWithMsg(Object data, boolean status, String msg) {
        return ApiResponse.createStatusResponseWithMsg(data, status, msg);
    }
}
