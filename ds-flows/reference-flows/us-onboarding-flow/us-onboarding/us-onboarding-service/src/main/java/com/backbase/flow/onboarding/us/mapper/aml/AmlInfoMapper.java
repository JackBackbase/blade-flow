package com.backbase.flow.onboarding.us.mapper.aml;

import com.backbase.flow.application.uso.casedata.OnboardingAntiMoneyLaunderingInfo;
import com.backbase.flow.service.aml.casedata.AntiMoneyLaunderingInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AmlInfoMapper {

    AmlInfoMapper INSTANCE = Mappers.getMapper(AmlInfoMapper.class);

    OnboardingAntiMoneyLaunderingInfo map(AntiMoneyLaunderingInfo antiMoneyLaunderingInfo);

    AntiMoneyLaunderingInfo map(OnboardingAntiMoneyLaunderingInfo antiMoneyLaunderingInfo);
}
