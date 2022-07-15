package com.backbase.flow.sme.onboarding.builder;


import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class BusinessInfoRequestBuilder {

    private String legalName;
    private String knownName;
    private String ein;
    private OffsetDateTime establishedDate;
    private String operationState;


}
