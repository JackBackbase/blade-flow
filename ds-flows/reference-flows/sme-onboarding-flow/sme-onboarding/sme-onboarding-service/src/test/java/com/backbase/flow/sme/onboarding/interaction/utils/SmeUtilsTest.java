package com.backbase.flow.sme.onboarding.interaction.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.backbase.flow.sme.onboarding.casedata.Address;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SmeUtilsTest {

    private final String applicantId = UUID.randomUUID().toString();

    @Test
    void validateAndReturnRegistrar_whenRegistrarSet_returnRegistrar() {
        var dob = LocalDate.now();
        var smeCase = new SmeCaseDefinition()
            .withApplicants(
                List.of(new Applicant()
                    .withFirstName("Name")
                    .withLastName("LastName")
                    .withDateOfBirth(LocalDate.now())
                    .withIsRegistrar(true)
                    .withEmail("test@gmail.com")
                )
            );

        var applicant = SmeUtils.validateAndReturnRegistrar(smeCase);

        assertSoftly(softly -> {
            softly.assertThat(applicant)
                .isNotNull();
            softly.assertThat(applicant.isPresent())
                .isTrue();
            var appl = applicant.get();
            softly.assertThat(appl.getFirstName())
                .isEqualTo("Name");
            softly.assertThat(appl.getLastName())
                .isEqualTo("LastName");
            softly.assertThat(appl.getDateOfBirth())
                .isEqualTo(LocalDate.now());
            softly.assertThat(appl.getEmail())
                .isEqualTo("test@gmail.com");
            softly.assertThat(appl.getIsRegistrar())
                .isTrue();
        });
    }

    @ParameterizedTest
    @MethodSource("invalidSmeApplicants")
    void validateAndReturnRegistrar_whenRegistrarNotSet_returnEmptyOptional(SmeCaseDefinition smeCase) {
        var applicant = SmeUtils.validateAndReturnRegistrar(smeCase);

        assertThat(applicant)
            .isEmpty();
    }

    private static Stream<Arguments> invalidSmeApplicants() {
        return Stream.of(
            Arguments.of(new SmeCaseDefinition()),
            Arguments.of(new SmeCaseDefinition().withApplicants(null)),
            Arguments.of(new SmeCaseDefinition().withApplicants(Collections.emptyList())),
            Arguments.of(new SmeCaseDefinition().withApplicants(Collections.singletonList(null))),
            Arguments.of(new SmeCaseDefinition().withApplicants(List.of(new Applicant()))),
            Arguments.of(new SmeCaseDefinition().withApplicants(List.of(new Applicant().withIsRegistrar(null)))),
            Arguments.of(new SmeCaseDefinition().withApplicants(List.of(new Applicant().withIsRegistrar(false))))
        );
    }

    @Test
    void validateAddressForApplicant_withAddress_returnTrue() {
        var smeCase = new SmeCaseDefinition().withApplicants(List.of(
            new Applicant()
                .withId(applicantId)
                .withPersonalAddress(new Address())
            ));

        var addressValidated = SmeUtils.isAddressValidatedForApplicant(smeCase, applicantId);
        assertTrue(addressValidated);
    }

    @Test
    void validateAddressForApplicant_withoutAddress_returnFalse() {
        var smeCase = caseWithApplicant();

        var addressValidated = SmeUtils.isAddressValidatedForApplicant(smeCase, applicantId);
        assertFalse(addressValidated);
    }

    @Test
    void getApplicant_correctId_returnApplicant() {
        var smeCase = caseWithApplicant();

        assertThat(SmeUtils.getApplicant(smeCase, applicantId)).isSameAs(smeCase.getApplicants().get(0));
    }

    @Test
    void getApplicant_incorrectId_throwsException() {
        var smeCase = caseWithApplicant();

        assertThatThrownBy(() -> SmeUtils.getApplicant(smeCase, "Incorrect"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("No applicant with id: Incorrect");
    }

    private SmeCaseDefinition caseWithApplicant() {
        return new SmeCaseDefinition().withApplicants(List.of(
            new Applicant().withId(applicantId)));
    }
}
