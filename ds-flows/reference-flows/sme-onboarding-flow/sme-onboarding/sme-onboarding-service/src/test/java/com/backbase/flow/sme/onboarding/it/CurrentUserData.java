package com.backbase.flow.sme.onboarding.it;

import com.backbase.flow.service.businessrelations.interaction.dto.BusinessPersonResponse;
import lombok.Value;

@Value
class CurrentUserData {
    BusinessPersonResponse currentUser;
    String selectedRole;
    double ownershipPercentage;
}
