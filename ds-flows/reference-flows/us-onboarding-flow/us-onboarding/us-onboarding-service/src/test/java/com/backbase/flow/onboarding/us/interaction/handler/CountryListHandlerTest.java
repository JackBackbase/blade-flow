package com.backbase.flow.onboarding.us.interaction.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.service.CountryService;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CountryListHandlerTest {

    private final InteractionContext interactionContext = mock(InteractionContext.class);
    private final CountryService countryService = mock(CountryService.class);
    public final CountryListHandler countryListHandler = new CountryListHandler(countryService);

    @Test
    void handle_returnsCountryListMap() {
        var defaultCountryListMap = Map.of(
            "CT1", "Country 1",
            "CT2", "Country 2",
            "CT3", "Country 3");

        when(countryService.getCountryListMap()).thenReturn(defaultCountryListMap);

        var result = countryListHandler.handle(null, interactionContext);

        assertThat(result.getBody()).containsAllEntriesOf(Map.of(
            "CT1", "Country 1",
            "CT2", "Country 2",
            "CT3", "Country 3"));
    }

}