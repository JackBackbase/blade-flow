package com.backbase.flow.onboarding.us.interaction.hook;

import static com.backbase.flow.service.ServiceItemOneTimePasswordAutoconfiguration.OTP_MAPPER_CONTEXT;

import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.interaction.engine.action.ActionHandlerHook;
import com.backbase.flow.interaction.engine.action.InteractionContext;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * {@link ActionHandlerHook} promoting preliminary case under condition specified for OTP journey. To promote the case
 * either {@code "phoneNumberVerified"} or {@code "emailVerified"} must exist and be set to {@code true} in the case
 * data.
 * <p>See also: {@link com.backbase.flow.service.interaction.hook.PreliminaryCasePromotionHook}.
 */
@Component("otp-verified-preliminary-case-promotion")
@RequiredArgsConstructor
public class OtpVerifiedPreliminaryCasePromotionHook implements ActionHandlerHook {

    private final CaseDataService caseDataService;
    @SuppressWarnings("rawtypes")
    private final JourneyMapper<Map> journeyMapper;

    public boolean execute(InteractionContext interactionContext) {

        final UUID caseKey = interactionContext.getCaseKey();

        if (this.caseDataService.getCaseByKey(caseKey).isPreliminary()) {

            @SuppressWarnings("unchecked")
            Map<String, Object> caseData = journeyMapper.read(null, OTP_MAPPER_CONTEXT, caseKey.toString());

            if (this.isOtpVerified(caseData)) {
                interactionContext.promoteCase();
                return true;
            }
            return false;
        }

        return true;
    }

    private boolean isOtpVerified(Map<String, Object> caseData) {
        return Boolean.TRUE.equals(caseData.get("phoneNumberVerified"))
            || Boolean.TRUE.equals(caseData.get("emailVerified"));
    }

}
