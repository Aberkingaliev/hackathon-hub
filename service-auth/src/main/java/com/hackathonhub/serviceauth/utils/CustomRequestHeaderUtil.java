package com.hackathonhub.serviceauth.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

public class CustomRequestHeaderUtil extends HttpServletRequestWrapper {

    private final HashMap<String, String> customHeaders;
    private final List<Cookie> customCookies;

    public CustomRequestHeaderUtil(HttpServletRequest request,
                                   HashMap<String, String> customHeaders,
                                   List<Cookie> customCookies) {
        super(request);
        this.customHeaders = customHeaders;
        this.customCookies = new ArrayList<>(Arrays.asList(request.getCookies()));
        this.customCookies.addAll(customCookies);

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            this.customHeaders.put(headerName, request.getHeader(headerName));
        }

        this.customHeaders.putAll(customHeaders);
    }

    @Override
    public Cookie[] getCookies() {
        return this.customCookies.toArray(new Cookie[0]);
    }

    public void setHeader(String key, String value) {
        if (this.customHeaders.containsKey(key)) {
            this.customHeaders.remove(key);
        }
        this.customHeaders.put(key, value);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(this.customHeaders.keySet());
    }

    @Override
    public String getHeader(String name) {
        return customHeaders.getOrDefault(name, super.getHeader(name));
    }
}
