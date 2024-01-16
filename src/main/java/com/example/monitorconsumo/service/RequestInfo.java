package com.example.monitorconsumo.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.monitorconsumo.configs.CustomHttpTraceFilter.CORRELATION_ID_HEADER_NAME;
import static com.example.monitorconsumo.configs.CustomHttpTraceFilter.CORRELATION_ID_LOG_VAR_NAME;

@Slf4j
public class RequestInfo {
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletRequest getRequest() {
        if (getServletRequestAttributes() == null) {
            throw new RuntimeException("Exception: ServletRequestAttributes is null");
        }
        return getServletRequestAttributes().getRequest();
    }


    public static String getHeader(String nome) {
        HttpServletRequest request = getRequest();
        Enumeration<String> it = request.getHeaderNames();
        while (it.hasMoreElements()) {
            String st = it.nextElement();
            if (st.equalsIgnoreCase(nome)) {
                return request.getHeader(st);
            }
        }
        return null;
    }

    public static Map<String, String> getHeaders() {
        HttpServletRequest request = getRequest();
        Enumeration<String> it = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (it.hasMoreElements()) {
            String st = it.nextElement();
            headers.put(st, request.getHeader(st));
        }

        try {
            var mdc = MDC.get(CORRELATION_ID_LOG_VAR_NAME);
            if (mdc != null) {
                headers.put(CORRELATION_ID_HEADER_NAME, mdc);
            }
        } catch (Exception e) {
            MDC.put("correlationId", headers.get(CORRELATION_ID_HEADER_NAME));
        }

        return headers;
    }

    public static Map<String, String> getParameters() {
        HttpServletRequest request = getRequest();
        Enumeration<String> it = request.getParameterNames();
        Map<String, String> headers = new HashMap<>();
        while (it.hasMoreElements()) {
            String st = it.nextElement();
            headers.put(st, request.getParameter(st));
        }
        return headers;
    }

    public static String getPath() {
        return getRequest().getRequestURI();
    }

    public static String getRequestId() {
        return getRequestId(null);
    }

    public static String getRequestId(String appName) {
        var requestId = MDC.get(CORRELATION_ID_LOG_VAR_NAME);

        if (requestId == null) {
            try {
                requestId = getHeader(CORRELATION_ID_HEADER_NAME);
                if (requestId != null) {
                    MDC.put(CORRELATION_ID_LOG_VAR_NAME, requestId);
                }
            } catch (Exception e) {
                // ignore
            }
        }

        if (requestId == null) {
            if (appName != null) {
                requestId = appName + "-" + UUID.randomUUID();
            } else {
                requestId = UUID.randomUUID().toString();
            }
            MDC.put(CORRELATION_ID_LOG_VAR_NAME, requestId);
        }
        return requestId;
    }
}
