package com.backbase.flow.onboarding.us.interaction.hook;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.application.uso.casedata.CitizenshipReview;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionHandlerHook;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("is-citizenship-review-needed")
public class CitizenshipReviewHook implements ActionHandlerHook {

    private final JourneyMapper<Applicant> journeyMapper;

    @Override
    public boolean execute(InteractionContext context) {
        UUID caseKey = context.getCaseKey();
        if (caseKey == null) {
            return false;
        }

        final Applicant applicant = journeyMapper.read(null, null, caseKey.toString());

        CitizenshipInfo citizenship = applicant.getCitizenship();
        if (citizenship == null || citizenship.getCitizenshipType() == null) {
            return false;
        }
        citizenship.setCitizenshipReview(
            new CitizenshipReview()
                .withNeeded(CitizenshipType.NON_RESIDENT_ALIEN.equals(citizenship.getCitizenshipType())));

        journeyMapper.write(applicant, null, null, caseKey.toString());
        return true;
    }
}
