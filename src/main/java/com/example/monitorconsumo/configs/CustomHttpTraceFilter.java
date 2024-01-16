package com.example.monitorconsumo.configs;

import com.example.monitorconsumo.dto.HttpTraceDTO;
import com.example.monitorconsumo.service.RequestInfo;
import com.example.monitorconsumo.utils.EnvReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@Component
@Slf4j
public class CustomHttpTraceFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID_HEADER_NAME = "x-correlation-id";
    public static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";

    private final int maxPayloadLength = 1000;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String getContentAsString(byte[] buf, int maxLength, String charsetName) {
        if (buf == null || buf.length == 0) return "";
        int length = Math.min(buf.length, this.maxPayloadLength);
        try {
            return new String(buf, 0, length, charsetName);
        } catch (UnsupportedEncodingException ex) {
            return "Unsupported Encoding";
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String[] notLogUrls = {"swagger", "/csrf", "/favicon.ico", "v2/api-docs", "actuator", "health", "prometheus"};


        if (Arrays.stream(notLogUrls).noneMatch(request.getRequestURL().toString()::contains)) {
            String appName = ApplicationInfo.getApplicationName();
            String appVersion = ApplicationInfo.getApplicationVersion();
            String correlationId = RequestInfo.getRequestId(appName);

            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            var startInstant = Instant.now();
            var wrappedRequest = new ContentCachingRequestWrapper(request);
            var wrappedResponse = new ContentCachingResponseWrapper(response);

            filterChain.doFilter(wrappedRequest, wrappedResponse);

            var endInstant = Instant.now();
            var durationSeconds = Duration.between(startInstant, endInstant).toMillis() / 1000.0;
            durationSeconds = Math.round(durationSeconds * 100.0) / 100.0;

            var httpTraceDTO = new HttpTraceDTO();
            httpTraceDTO.setMethod(request.getMethod());
            httpTraceDTO.setPath(request.getRequestURI());
            httpTraceDTO.setDuration(durationSeconds + "s");
            httpTraceDTO.setArgs(request.getQueryString());
            httpTraceDTO.setStatus(response.getStatus());
            httpTraceDTO.setAppName(appName);
            httpTraceDTO.setAppVersion(appVersion);
            httpTraceDTO.setRequestId(correlationId);

            var logSuccesBody = "S".equalsIgnoreCase(EnvReader.readEnv("LOG_SUCCESS_BODY"));
            if (logSuccesBody) {
                httpTraceDTO.setResponse(getContentAsString(wrappedResponse.getContentAsByteArray(),
                        this.maxPayloadLength, response.getCharacterEncoding()));

                httpTraceDTO.setRequest(getContentAsString(wrappedRequest.getContentAsByteArray(),
                        this.maxPayloadLength, request.getCharacterEncoding()));
            }

            log.info(objectMapper.writeValueAsString(httpTraceDTO));

            MDC.remove(CORRELATION_ID_LOG_VAR_NAME);

            wrappedResponse.copyBodyToResponse();
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
