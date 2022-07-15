package com.backbase.flow.onboarding.us.interaction.hook;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.application.uso.casedata.CitizenshipReview;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CitizenshipReviewHookTest {

    private final JourneyMapper<Applicant> mapper = mock(JourneyMapper.class);
    private final InteractionContext context = mock(InteractionContext.class);
    private final UUID caseKey = UUID.randomUUID();
    private final CitizenshipReviewHook hook = new CitizenshipReviewHook(mapper);

    @ParameterizedTest
    @EnumSource(CitizenshipType.class)
    void reviewCreated_ForEveryCitizenshipType(CitizenshipType type) {
        Applicant applicant = new Applicant()
            .withCitizenship(new CitizenshipInfo().withCitizenshipType(type));
        when(context.getCaseKey()).thenReturn(caseKey);
        when(mapper.read(null, null, caseKey.toString())).thenReturn(applicant);

        boolean result = hook.execute(context);

        assertThat(result).isTrue();
        Applicant expected = new Applicant()
            .withCitizenship(new CitizenshipInfo()
                .withCitizenshipType(type)
                .withCitizenshipReview(
                    new CitizenshipReview().withNeeded(CitizenshipType.NON_RESIDENT_ALIEN.equals(type)))
            );
        verify(mapper, times(1)).write(expected, null, null, caseKey.toString());
    }

    @Test
    void hookFailsIfThereIsNoType() {
        Applicant applicant = new Applicant()
            .withCitizenship(new CitizenshipInfo());
        when(context.getCaseKey()).thenReturn(caseKey);
        when(mapper.read(null, null, caseKey.toString())).thenReturn(applicant);

        boolean result = hook.execute(context);

        assertThat(result).isFalse();
    }

    @Test
    void hookFailsIfThereIsNoCitizenshipInfo() {
        Applicant applicant = new Applicant();
        when(context.getCaseKey()).thenReturn(caseKey);
        when(mapper.read(null, null, caseKey.toString())).thenReturn(applicant);

        boolean result = hook.execute(context);

        assertThat(result).isFalse();
    }
}