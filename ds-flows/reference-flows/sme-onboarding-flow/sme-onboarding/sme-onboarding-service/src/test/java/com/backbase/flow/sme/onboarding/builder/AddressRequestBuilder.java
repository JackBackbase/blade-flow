package com.backbase.flow.sme.onboarding.builder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddressRequestBuilder {

    private String numberAndStreet;
    private String apt;
    private String zipCode;
    private String city;
    private String state;
    private String addressName;

}
