package com.example.monitorconsumo.configs;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileReader;

@Slf4j
public class ApplicationInfo {
    private static String applicationName = null;
    private static String applicationVersion = null;

    public static String getApplicationVersion() {
        if (applicationVersion == null) {
            initNames();
        }
        return applicationVersion;
    }

    public static String getApplicationName() {
        if (applicationName == null) {
            initNames();
        }
        return applicationName;
    }


    public static void initNames() {
        var reader = new MavenXpp3Reader();
        try (FileReader fileReader = new FileReader("pom.xml")) {
            var model = reader.read(fileReader);
            applicationVersion = model.getVersion();
            applicationName = model.getArtifactId();
        } catch (Exception e) {
            log.error("Error reading pom.xml: {}", e.getMessage());
        }
    }
}
