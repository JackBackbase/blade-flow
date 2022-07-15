package com.backbase.flow.onboarding.us.process.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.application.uso.casedata.CitizenshipReview;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.process.ProcessConstants;
import java.util.UUID;
import java.util.stream.Stream;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class CitizenshipConditionalHandlerTest {

    private final JourneyMapper<Applicant> mapper = mock(JourneyMapper.class);
    private final DelegateExecution execution = mock(DelegateExecution.class);
    private final String caseKey = UUID.randomUUID().toString();
    private final CitizenshipConditionalHandler handler = new CitizenshipConditionalHandler(mapper);

    @ParameterizedTest
    @MethodSource("getPositiveScenarios")
    void citizenshipDataReadyForReview_true_ifNeededIsSet(Applicant applicant) {
        when(execution.getVariable(ProcessConstants.PROCESS_VARIABLE_CASE_KEY)).thenReturn(caseKey);
        when(mapper.read(null, null, caseKey)).thenReturn(applicant);

        boolean result = handler.citizenshipDataReadyForReview(execution);

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getFailed")
    void citizenshipDataReadyForReview_false_IfLacksCitizenshipData(Applicant applicant) {
        when(execution.getVariable(ProcessConstants.PROCESS_VARIABLE_CASE_KEY)).thenReturn(caseKey);
        when(mapper.read(null, null, caseKey)).thenReturn(applicant);

        boolean result = handler.citizenshipDataReadyForReview(execution);

        assertThat(result).isFalse();
    }

    @Test
    void isReviewNeeded_true_ifNeededIsSetToTrue() {
        Applicant applicant = new Applicant()
            .withCitizenship(new CitizenshipInfo()
                .withCitizenshipReview(new CitizenshipReview()
                    .withNeeded(true))
            );
        when(execution.getVariable(ProcessConstants.PROCESS_VARIABLE_CASE_KEY)).thenReturn(caseKey);
        when(mapper.read(null, null, caseKey)).thenReturn(applicant);

        boolean result = handler.isReviewNeeded(execution);

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(booleans = {false})
    void isReviewNeeded_false_ifNeededIsSetToFalseOrNull(Boolean needed) {
        Applicant applicant = new Applicant()
            .withCitizenship(new CitizenshipInfo()
                .withCitizenshipReview(new CitizenshipReview()
                    .withNeeded(needed))
            );
        when(execution.getVariable(ProcessConstants.PROCESS_VARIABLE_CASE_KEY)).thenReturn(caseKey);
        when(mapper.read(null, null, caseKey)).thenReturn(applicant);

        boolean result = handler.isReviewNeeded(execution);

        assertThat(result).isFalse();
    }

    private static Stream<Arguments> getPositiveScenarios() {
        return Stream.of(
            Arguments.of(new Applicant()
                .withCitizenship(new CitizenshipInfo()
                    .withCitizenshipReview(new CitizenshipReview()
                        .withNeeded(true))
                )),
            Arguments.of(new Applicant()
                .withCitizenship(new CitizenshipInfo()
                    .withCitizenshipReview(new CitizenshipReview()
                        .withNeeded(false))
                ))
        );
    }

    private static Stream<Arguments> getFailed() {
        return Stream.of(
            Arguments.of(new Applicant()),
            Arguments.of(new Applicant().withCitizenship(new CitizenshipInfo())),
            Arguments.of(new Applicant()
                .withCitizenship(new CitizenshipInfo().withCitizenshipReview(new CitizenshipReview())))
        );
    }

}