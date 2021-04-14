package com.teamteach.commons.connectors.http;

import com.google.common.base.Strings;
import com.teamteach.commons.security.authentication.AppAuthenticationToken;
import com.teamteach.commons.utils.AppSecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Component
public class RestTemplateWrapper {

    @Autowired
    RestTemplate restTemplate;

    public <T> T delete (URI resourceUri , Class<T> clazz , Map<String,String> headers) {
        try {
            final HttpHeaders httpHeaders = createHeaders(headers);
            //Execute the method writing your HttpEntity to the request
            final HttpEntity<T> entity = new HttpEntity(null,httpHeaders);
            ResponseEntity<T> response =
                    restTemplate.exchange(resourceUri, HttpMethod.DELETE,entity,clazz);
            return response.getBody();
        } catch (Exception e){
            System.out.println("Error Invoking Calender API");
            return null;
        }
    }


    public <T> T get(URI resourceUri, Class<T> clazz, Map<String, String> headers) {
        try {
            final HttpHeaders httpHeaders = createHeaders(headers);
            //Execute the method writing your HttpEntity to the request
            final HttpEntity<T> entity = new HttpEntity(null, httpHeaders);
            ResponseEntity<T> response =
                    restTemplate.exchange(resourceUri, HttpMethod.GET, entity, clazz);
            return response.getBody();
        } catch (Exception e) {
            System.out.println("Error Invoking URL Get :: " + resourceUri.toString());
            return null;
        }
    }

    public <T> T get (URI resourceUri, ParameterizedTypeReference<T> clazz, Map<String, String> h ) {
        try {
            final HttpHeaders headers = createHeaders(h);
            headers.set("Content-Type", "application/json");
            final HttpEntity<T> entity = new HttpEntity(null,headers);
            ResponseEntity<T> response =
                    restTemplate.exchange(resourceUri, HttpMethod.GET,entity,clazz);
            return response.getBody();
        } catch (Exception e){
            System.out.println("Error !!"+e.getMessage());
            System.out.println("Error Invoking Calender API");
            return null;
        }
    }

    public <T, T1> T post(String resourceUrl, T1 body, Map<String, String> headers,
                          ParameterizedTypeReference<T> clazz) {
        final HttpHeaders httpHeaders = createHeaders(headers);
        final HttpEntity<T> entity = new HttpEntity(body, httpHeaders);
        ResponseEntity<T> response =
                restTemplate.exchange(resourceUrl, HttpMethod.POST, entity, clazz);
        return response.getBody();
    }

    public <T, T1> T put(String resourceUrl, T1 body, Map<String, String> headers,
                         ParameterizedTypeReference<T> clazz) {
        final HttpHeaders httpHeaders = createHeaders(headers);
        final HttpEntity<T> entity = new HttpEntity(body, httpHeaders);
        ResponseEntity<T> response =
                restTemplate.exchange(resourceUrl, HttpMethod.PUT, entity, clazz);
        return response.getBody();
    }

    public <T> T uploadFile(String url,
                            Map<String,Object> bodyParams ,
                            Map<String, String> headers,
                            ParameterizedTypeReference<T> clazz ) {
        HttpHeaders httpHeaders = createHeaders(headers);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        if (bodyParams != null) {
            bodyParams.keySet().forEach(a -> {
                body.add(a, bodyParams.get(a));
            });
        }
        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body,httpHeaders);
        ResponseEntity<T> response =
                restTemplate.exchange(url, HttpMethod.POST, requestEntity, clazz);
        return response.getBody();
    }

	private HttpHeaders createHeaders(Map<String, String> headers) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		final Map<String, String> headersToSet = this.attachJwtTokenIfPresent(headers);
		if (headersToSet != null) {
			headersToSet.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			headersToSet.keySet().forEach(a -> httpHeaders.set(a, headersToSet.get(a)));
		}
		return httpHeaders;
	}

    private Map<String, String> attachJwtTokenIfPresent(Map<String, String> headers) {
        Map<String, String> clonedHeaders = new HashMap<>();
        if (headers != null)
            clonedHeaders.putAll(headers);
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AppAuthenticationToken) {
            String token = ((AppAuthenticationToken)authentication).getToken();
            if (!Strings.isNullOrEmpty(token))
                clonedHeaders.put( AppSecurityConstants.AUTHORIZATION_HEADER,
                        String.format("%s %s", AppSecurityConstants.AUTHORIZATION_HEADER_TOKEN_PREFIX,token));
        }
        return clonedHeaders;
    }

}
