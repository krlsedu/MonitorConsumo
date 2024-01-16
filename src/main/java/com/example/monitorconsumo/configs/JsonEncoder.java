package com.example.monitorconsumo.configs;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.EncoderBase;
import com.example.monitorconsumo.dto.HttpTraceDTO;
import com.example.monitorconsumo.service.RequestInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonEncoder extends EncoderBase<ILoggingEvent> {
    private final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public byte[] headerBytes() {
        return new byte[0];
    }

    @Override
    public byte[] encode(ILoggingEvent iLoggingEvent) {
        var log = new HttpTraceDTO();
        try {
            log = mapper.readValue(iLoggingEvent.getFormattedMessage(), HttpTraceDTO.class);
        } catch (Exception e) {
            log.setMsg(iLoggingEvent.getFormattedMessage());
        }
        log.setName(iLoggingEvent.getLoggerName());
        log.setType(iLoggingEvent.getLevel().toString());
        log.setRequestId(RequestInfo.getRequestId());
        log.setAppName(ApplicationInfo.getApplicationName());
        log.setAppVersion(ApplicationInfo.getApplicationVersion());
        try {
            return (mapper.writeValueAsString(log) + "\n").getBytes();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] footerBytes() {
        return new byte[0];
    }
}
