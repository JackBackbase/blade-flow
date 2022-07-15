package com.backbase.flow.onboarding.us.interaction.hook;

import static com.backbase.flow.service.ServiceItemOneTimePasswordAutoconfiguration.OTP_MAPPER_CONTEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OtpVerifiedPreliminaryCasePromotionHookTest {

    private final InteractionContext context = mock(InteractionContext.class);
    private final CaseDataService caseDataService = mock(CaseDataService.class);
    @SuppressWarnings("rawtypes")
    private final JourneyMapper<Map> journeyMapper = mock(JourneyMapper.class);

    private final OtpVerifiedPreliminaryCasePromotionHook preliminaryCasePromotionHook =
        new OtpVerifiedPreliminaryCasePromotionHook(caseDataService, journeyMapper);

    private void arrange(Boolean isPreliminary, Boolean phoneNumberVerified, Boolean emailVerified) {
        final UUID caseKey = UUID.randomUUID();
        Case preliminaryCase = new Case().setPreliminary(isPreliminary);
        given(context.getCaseKey()).willReturn(caseKey);
        given(caseDataService.getCaseByKey(caseKey)).willReturn(preliminaryCase);

        if (phoneNumberVerified != null && emailVerified != null) {
            Map<String, Object> caseData = new HashMap<>();
            caseData.put("phoneNumberVerified", phoneNumberVerified);
            caseData.put("emailVerified", emailVerified);
            given(journeyMapper.read(null, OTP_MAPPER_CONTEXT, caseKey.toString())).willReturn(caseData);
        }
    }

    @Test
    void execute_CaseIsPreliminary_AndEmailOtpIsVerified_CaseIsPromoted() {
        arrange(true, false, true);

        var result = preliminaryCasePromotionHook.execute(context);

        assertThat(result).isTrue();
        verify(context).promoteCase();
    }

    @Test
    void execute_CaseIsPreliminary_AndPhoneNumberIsOtpVerified_CaseIsPromoted() {
        arrange(true, true, false);

        var result = preliminaryCasePromotionHook.execute(context);

        assertThat(result).isTrue();
        verify(context).promoteCase();
    }

    @Test
    void execute_CaseIsPreliminary_AndOtpIsNotVerified_CaseIsNotPromoted() {
        arrange(true, false, false);

        var result = preliminaryCasePromotionHook.execute(context);

        assertThat(result).isFalse();
        verify(context, never()).promoteCase();
    }

    @Test
    void execute_CaseIsNotPreliminary_CaseIsNotPromoted() {
        arrange(false, null, null);

        var result = preliminaryCasePromotionHook.execute(context);

        assertThat(result).isTrue();
        verify(context, never()).promoteCase();
    }
}