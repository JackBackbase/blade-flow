package com.backbase.flow.onboarding.us.mapper.address;

import static org.assertj.core.api.Assertions.assertThat;

import com.backbase.flow.application.uso.casedata.Address;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class AddressMapperTest {

    AddressMapper MAPPER = Mappers.getMapper(AddressMapper.class);

    @Test
    void map() {
        final Address address = new Address()
            .withState("state")
            .withCity("city")
            .withNumberAndStreet("numnum")
            .withZipCode("zipp")
            .withApt("apt");

        final var result = MAPPER.map(address);

        assertThat(result.getAddress().getState()).isEqualTo("state");
    }
}
