package com.example.monitorconsumo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpTraceDTO {
    private String method;
    private String path;
    private Integer status;
    private String duration;
    private Object args;
    private Object response;
    private Object request;
    private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private String name;
    private String type;
    private String msg;
    private String requestId;
    private String appName;
    private String appVersion;
}
