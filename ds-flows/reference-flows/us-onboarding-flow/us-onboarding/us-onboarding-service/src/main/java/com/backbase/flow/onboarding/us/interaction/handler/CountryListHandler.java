package com.backbase.flow.onboarding.us.interaction.handler;

import com.backbase.flow.interaction.engine.action.ActionHandler;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.service.CountryService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("country-list")
@RequiredArgsConstructor
public class CountryListHandler implements ActionHandler<Void, Map<String, String>> {

    private final CountryService countryService;

    @Override
    public ActionResult<Map<String, String>> handle(Void payload, InteractionContext context) {
        var countryListMap = countryService.getCountryListMap();
        return new ActionResult<>(countryListMap);
    }
}
