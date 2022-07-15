package com.backbase.flow.onboarding.us.interaction.handler;

import static com.backbase.flow.onboarding.us.validation.ValidationConstants.SSN_VALIDATION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo;
import com.backbase.flow.application.uso.casedata.CitizenshipInfo.CitizenshipType;
import com.backbase.flow.application.uso.casedata.Onboarding;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.casedata.mapper.JourneyReader;
import com.backbase.flow.casedata.mapper.JourneyWriter;
import com.backbase.flow.interaction.engine.action.ActionResult;
import com.backbase.flow.interaction.engine.action.ErrorCodes;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.onboarding.us.interaction.dto.OnboardingDto;
import com.backbase.flow.onboarding.us.interaction.dto.SsnDto;
import com.backbase.flow.onboarding.us.mapper.onboarding.OnboardingReader;
import com.backbase.flow.onboarding.us.mapper.onboarding.OnboardingWriter;
import com.backbase.flow.utils.CaseDataUtils;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

class SubmitSsnHandlerTest {

    private final InteractionContext interactionContext = mock(InteractionContext.class);
    private final CaseDataUtils caseDataUtils = mock(CaseDataUtils.class);
    private final CaseDataService caseDataService = mock(CaseDataService.class);

    private final JourneyReader<Onboarding> reader = new OnboardingReader();
    private final JourneyWriter<Onboarding> writer = new OnboardingWriter(caseDataService);
    private final JourneyMapper<Onboarding> mapper = spy(new JourneyMapper<>(Onboarding.class,
        List.of(reader), List.of(writer), caseDataService));
    private final SubmitSsnHandler submitSsnHandler = new SubmitSsnHandler(mapper, caseDataUtils);

    @ParameterizedTest
    @ValueSource(strings = {"123456789", "213456785", "078051121", "665345697", "001234456", "123021234", "145755546"})
    void handleWithChecks(final String ssn) {
        Mockito.reset(interactionContext);
        final SsnDto ssnDto = new SsnDto(ssn);
        final Onboarding caseData = new Onboarding().withMainApplicant(new Applicant());
        caseData.setIsMainApplicantFlow(true);
        caseData.getMainApplicant().setCitizenship(new CitizenshipInfo()
            .withCitizenshipType(CitizenshipType.US_CITIZEN)
            .withSsn(ssn));
        setupCase(caseData);

        final ActionResult<OnboardingDto> actionResult = submitSsnHandler.handle(ssnDto, interactionContext);

        assertThat(actionResult.getBody().getSsn()).isEqualTo(ssnDto.getSsn());
        assertThat(actionResult.getErrors()).isEmpty();

        verify(mapper, times(1)).write(eq(caseData), any(), any(), anyString());
    }

    @Test
    void validationWorks() {
        assertSsnValueIsIncorrect(null); // cannot be null
        assertSsnValueIsIncorrect(""); // cannot be empty
        assertSsnValueIsIncorrect("12345678"); // cannot be less than 9 symbols length
        assertSsnValueIsIncorrect("078-05-1120"); // cannot contain dashes
        assertSsnValueIsIncorrect("123 45 6789"); // cannot contain spaces
        assertSsnValueIsIncorrect("123.45.6789"); // cannot contain dots
        assertSsnValueIsIncorrect("1234567890"); // cannot exceed 9 symbols length
        assertSsnValueIsIncorrect("078051120"); // value not allowed due to Woolworth’s Wallet Fiasco
        assertSsnValueIsIncorrect("219099999"); // appeared in an advertisement for the Social Security Administration
        assertSsnValueIsIncorrect("666345697"); // cannot begin with 666
        assertSsnValueIsIncorrect("000234456"); // cannot have zeros only in the first part
        assertSsnValueIsIncorrect("123001234"); // cannot have zeros only in the middle part
        assertSsnValueIsIncorrect("123040000"); // cannot have zeros only in the last part
    }

    @Test
    void coApplicantFlow_returnsCoApplicantData() {
        final SsnDto ssnDto = new SsnDto("123456789");
        final Onboarding caseData = new Onboarding().withMainApplicant(new Applicant());
        final Applicant coApplicant = new Applicant();
        coApplicant.setCitizenship(new CitizenshipInfo()
            .withCitizenshipType(CitizenshipType.US_CITIZEN)
            .withSsn(ssnDto.getSsn()));
        caseData.setCoApplicant(coApplicant);
        caseData.getMainApplicant().setCitizenship(new CitizenshipInfo()
            .withCitizenshipType(CitizenshipType.US_CITIZEN)
            .withSsn("987654321"));
        caseData.setCoApplicant(coApplicant);
        caseData.setCoApplicantId("abc-123");
        setupCase(caseData);

        final ActionResult<OnboardingDto> actionResult = submitSsnHandler.handle(ssnDto, interactionContext);

        assertThat(actionResult.getBody().getSsn()).isEqualTo(ssnDto.getSsn());
    }

    @Test
    void handleWithEmptyCitizenshipInfo() {
        handleWithEmptyCitizenshipInfoAndType(new Applicant());
    }

    @Test
    void handleWithEmptyCitizenshipInfoAndType() {
        handleWithEmptyCitizenshipInfoAndType(new Applicant().withCitizenship(new CitizenshipInfo()));
    }

    void handleWithEmptyCitizenshipInfoAndType(Applicant applicant) {
        Mockito.reset(interactionContext);
        final SsnDto ssnDto = new SsnDto("123456789");
        final Onboarding caseData = new Onboarding().withMainApplicant(applicant);
        caseData.setIsMainApplicantFlow(true);
        setupCase(caseData);

        ActionResult<OnboardingDto> result = submitSsnHandler.handle(ssnDto, interactionContext);

        assertThat(result.getBody()).isNull();
        assertThat(result.getErrors().size()).isEqualTo(1);
        assertThat(result.getErrors()).anyMatch(error -> error.getMessage().equals("Citizenship type must be defined before submit SSN"));
        verify(mapper, never()).write(eq(caseData), any(), any(), anyString());
    }

    private void assertSsnValueIsIncorrect(final String ssn) {
        final SsnDto ssnDto = new SsnDto(ssn);

        final ActionResult<OnboardingDto> actionResult = submitSsnHandler.handle(ssnDto, interactionContext);

        final List<Error> errors = actionResult.getErrors();
        assertThat(errors).isNotEmpty();
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0).getKey()).isEqualTo(ErrorCodes.FLOW_001.getKey());
        assertThat(errors.get(0).getMessage()).isEqualTo(ErrorCodes.FLOW_001.getMessage());
        assertThat(errors.get(0).getContext().get("ssn")).contains(SSN_VALIDATION_MESSAGE);

        verify(mapper, never()).write(any(), any(), any());
    }

    private void setupCase(Onboarding onboarding) {
        final UUID caseKey = UUID.randomUUID();
        when(caseDataUtils.getOrCreateCaseKey(any())).thenReturn(caseKey);

        final Case sampleCase = new Case();
        sampleCase.setCaseData(onboarding);
        when(caseDataService.getCaseByKey(caseKey)).thenReturn(sampleCase);

        when(caseDataService.updateCase(sampleCase)).thenReturn(sampleCase);
    }
}
