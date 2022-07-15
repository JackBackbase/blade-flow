package com.backbase.flow.onboarding.us.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryService {

    private final ObjectMapper objectMapper;

    @Value("${backbase.flow.citizenship.country-list.file-path:/countries/countryList.json}")
    private String countryListFilePath;

    @SneakyThrows(IOException.class)
    public Map<String, String> getCountryListMap() {

        log.debug("Getting country list configuration file {}", countryListFilePath);
        try (var inputStream = new ClassPathResource(countryListFilePath).getInputStream()) {
            //noinspection unchecked
            return objectMapper.readValue(inputStream, Map.class);
        }
    }
}
