package com.backbase.flow.sme.onboarding.interaction.model;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDate;

@Value
@Builder
public class AnchorRequestDto {

    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String emailAddress;
    String phoneNumber;
}
