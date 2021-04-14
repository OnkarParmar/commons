package com.teamteach.commons.connectors.http;

public class HttpAuthInfo {
    Object authenticationDetails;
    private AuthTypes authType;

    enum AuthTypes {
        BASIC
    }
}
