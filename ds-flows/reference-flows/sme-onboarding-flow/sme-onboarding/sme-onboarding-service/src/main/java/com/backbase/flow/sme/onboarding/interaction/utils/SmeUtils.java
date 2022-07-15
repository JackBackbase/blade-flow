package com.backbase.flow.sme.onboarding.interaction.utils;

import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SmeUtils {

    public static Optional<Applicant> validateAndReturnRegistrar(SmeCaseDefinition smeCase) {
        return getApplicants(smeCase)
            .filter(applicant -> Optional.ofNullable(applicant.getIsRegistrar()).orElse(false))
            .findFirst();
    }

    public static Applicant getRegistrar(SmeCaseDefinition smeCase) {
        return validateAndReturnRegistrar(smeCase).orElseThrow();
    }

    public static boolean isAddressValidatedForApplicant(SmeCaseDefinition smeCase, String id) {
        var applicant = getApplicant(smeCase, id);
        return applicant.getPersonalAddress() != null;
    }

    public static Applicant getApplicant(SmeCaseDefinition smeCase, String id) {
        return getApplicants(smeCase)
            .filter(applicant -> id.equals(applicant.getId()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No applicant with id: " + id));
    }

    private static Stream<Applicant> getApplicants(SmeCaseDefinition smeCase) {
        return Optional.ofNullable(smeCase.getApplicants())
            .stream()
            .flatMap(Collection::stream)
            .filter(Objects::nonNull);
    }
}
