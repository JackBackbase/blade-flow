package com.backbase.flow.onboarding.us.interaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchCustomerDataResponseDto {

    private Boolean isMainApplicant;

    private Address address;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address {

        private String numberAndStreet;
        private String zipCode;
        private String city;
        private String state;
        private String apt;
    }

}
