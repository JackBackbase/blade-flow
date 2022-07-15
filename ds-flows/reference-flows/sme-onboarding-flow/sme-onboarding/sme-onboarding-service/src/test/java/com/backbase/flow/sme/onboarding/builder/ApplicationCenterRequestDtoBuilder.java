package com.backbase.flow.sme.onboarding.builder;

import com.backbase.flow.sme.onboarding.interaction.model.ApplicationCenterRequestDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationCenterRequestDtoBuilder {

    private String id;

    public static ApplicationCenterRequestDtoBuilder getInstance() {
        return new ApplicationCenterRequestDtoBuilder();
    }

    public ApplicationCenterRequestDtoBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ApplicationCenterRequestDto build() {
        ApplicationCenterRequestDto dto = new ApplicationCenterRequestDto();
        dto.setId(id);
        return dto;
    }
}
