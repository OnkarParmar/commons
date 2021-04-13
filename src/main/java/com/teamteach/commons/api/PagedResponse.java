package com.teamteach.commons.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class PagedResponse<T> {
    boolean status;
    int code;
    String msg;
    Map<String,String> links;
    Collection<T> data;
    int size;

    private PagedResponse(boolean status, int code) {
        this.status = status;
        this.code = code;
    }

    private PagedResponse(boolean status, int code, Map<String,String> links, Collection<T> data) {
        this(status, code);
        this.links = links;
        this.data = data;
        this.size = data.size();
    }

    public static <T> PagedResponse<T> getPagedResponse(Collection<T> data) {
        return new PagedResponse(true, HttpStatus.OK.value(), Collections.emptyMap(), data);
    }

    public static <T> PagedResponse<T> getPagedResponse(int totalPages, int curPage, long size, Collection<T> data) {
        String next = "";
        String prev = "";
        String sizeParam = "&size=" + size;
        if (curPage < totalPages) {
            next = "page=" + (curPage + 1) + sizeParam;
        }
        if (curPage > 1) {
            prev = "page=" + (curPage - 1) + sizeParam;
        }
        return new PagedResponse(true, HttpStatus.OK.value(), Map.of("next", next, "prev", prev), data);
    }
}
