package com.teamteach.commons.connectors.http.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;
import java.nio.charset.StandardCharsets;

public class HttpUtils {

    public static String generateBasicAuthHeader(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);
        return authHeader;
    }

    public static URI generateURI(String url, String query) {
        DefaultUriBuilderFactory builderFactory = new DefaultUriBuilderFactory();
        builderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);        // Query parameters
        URI uri = builderFactory.uriString(url).build(query);
        return uri;
    }
}
