package com.kvitka.subtaskbuilder.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PostStartConfiguration {

    @Value("${properties.manager-url}")
    private String managerUrl;
    @Value("${properties.send-jar-method}")
    private String sendJarMethod;
    @Value("${properties.jar-path}")
    private String jarPath;

    private final RestTemplate restTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() throws IOException {
        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("jar", new MockMultipartFile("jar-file.jar", "jar-file.jar",
                "application/java-archive", Files.readAllBytes(Paths.get(jarPath))).getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        restTemplate.put(managerUrl + sendJarMethod, new HttpEntity<>(requestMap, headers));
    }
}
