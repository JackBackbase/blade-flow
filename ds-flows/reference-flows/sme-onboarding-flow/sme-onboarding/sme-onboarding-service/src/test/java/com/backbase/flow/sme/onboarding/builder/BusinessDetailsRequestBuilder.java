package com.backbase.flow.sme.onboarding.builder;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class BusinessDetailsRequestBuilder {

    private String industryKey;

    private String industryValue;

    private String nature;

    private String website;


}
