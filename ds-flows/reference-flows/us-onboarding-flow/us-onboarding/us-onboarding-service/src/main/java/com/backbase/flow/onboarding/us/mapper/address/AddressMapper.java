package com.backbase.flow.onboarding.us.mapper.address;


import com.backbase.flow.application.uso.casedata.Address;
import com.backbase.flow.service.addressvalidation.casedata.AddressValidationDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
interface AddressMapper {

    AddressMapper MAPPER = Mappers.getMapper(AddressMapper.class);

    @Mapping(target = "address.numberAndStreet", source = "numberAndStreet")
    @Mapping(target = "address.zipCode", source = "zipCode")
    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "address.state", source = "state")
    @Mapping(target = "address.apt", source = "apt")
    AddressValidationDefinition map(Address address);

}
