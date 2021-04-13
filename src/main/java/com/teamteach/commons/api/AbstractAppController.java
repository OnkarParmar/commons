package com.teamteach.commons.api;

public class AbstractAppController {
    protected ApiResponse sendSuccessResponse(Object data) {
        return ApiResponse.createSuccessResponse(data);
    }
}
