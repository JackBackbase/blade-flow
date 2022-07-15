package com.backbase.flow.onboarding.us.util;

import com.backbase.flow.application.uso.casedata.Applicant;
import com.backbase.flow.application.uso.casedata.Onboarding;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OnboardingCaseDataUtils {

    private static final String CO_APPLICANT_ID = "coApplicantId";
    private static final String MAIN_APPLICANT = "mainApplicant";
    private static final String CO_APPLICANT = "coApplicant";

    public static boolean isMainApplicantFlow(Map<String, Object> onboarding) {
        return !onboarding.containsKey(CO_APPLICANT_ID);
    }

    public static boolean isMainApplicantFlow(Onboarding onboarding) {
        return Boolean.TRUE.equals(onboarding.getIsMainApplicantFlow());
    }

    public static Map<String, Object> getApplicant(Map<String, Object> onboarding) {
        //noinspection unchecked
        return (Map<String, Object>) (isMainApplicantFlow(onboarding)
            ? onboarding.get(MAIN_APPLICANT)
            : onboarding.get(CO_APPLICANT));
    }

    public static void setApplicant(Map<String, Object> onboarding, Map<String, Object> applicant) {
        if (isMainApplicantFlow(onboarding)) {
            onboarding.put(MAIN_APPLICANT, applicant);
        } else {
            onboarding.put(CO_APPLICANT, applicant);
        }
    }

    public static Applicant getApplicant(Onboarding onboarding) {
        return isMainApplicantFlow(onboarding)
            ? onboarding.getMainApplicant()
            : onboarding.getCoApplicant();
    }

    public static void setApplicant(Onboarding onboarding, Applicant applicant) {
        if (isMainApplicantFlow(onboarding)) {
            onboarding.setMainApplicant(applicant);
        } else {
            onboarding.setCoApplicant(applicant);
        }
    }
}
