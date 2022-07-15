package com.backbase.flow.onboarding.us.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ObjectMapper.class, CountryService.class})
class CountryServiceTest {

    @Autowired
    public CountryService countryService;

    @Test
    void getCountryListMap() {
        var result = countryService.getCountryListMap();

        assertThat(result).containsAllEntriesOf(Map.of(
            "CT1", "Country 1",
            "CT2", "Country 2",
            "CT3", "Country 3"));
    }
}