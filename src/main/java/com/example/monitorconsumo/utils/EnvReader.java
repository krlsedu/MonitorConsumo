package com.example.monitorconsumo.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class EnvReader {
    public static String readEnv(String envName) {
        String envValue = null;
        try {
            //read value from envoriment
            envValue = System.getenv(envName);
        } catch (Exception e) {
            log.error("Error reading envoriment variable " + envName, e);
        }
        return envValue;
    }
}
