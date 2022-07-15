package com.backbase.flow.sme.onboarding.process.service;

import static com.backbase.flow.sme.onboarding.constants.ProcessConstants.DMN_DECIDED_ON_BUSINESS_RELATION_REQUIRED;

import com.backbase.flow.interaction.engine.action.InteractionContext;
import com.backbase.flow.sme.onboarding.casedata.BusinessStructureInfo;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.constants.ProcessConstants;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SmeBusinessRelationService {

    public boolean isBusinessRelationRequired(SmeCaseDefinition smeCase, InteractionContext context) {
        var businessStructureInfo = smeCase.getCompanyLookupInfo().getBusinessStructureInfo();
        var outputs = context.checkDecisionTable(ProcessConstants.DMN_DECIDED_ON_BUSINESS_RELATION, null,
            prepareInputs(businessStructureInfo));
        return (boolean) outputs.get(0).get(DMN_DECIDED_ON_BUSINESS_RELATION_REQUIRED);
    }

    private Map<String, Object> prepareInputs(BusinessStructureInfo businessStructureInfo) {
        var businessType = businessStructureInfo.getType().value();
        var businessSubType =
            businessStructureInfo.getSubtype() == null ? null : businessStructureInfo.getSubtype().value();

        var inputs = new HashMap<String, Object>();
        inputs.put(ProcessConstants.DMN_DECIDED_ON_BUSINESS_RELATION_BUSINESS_STRUCTURE, businessType);
        inputs.put(ProcessConstants.DMN_DECIDED_ON_BUSINESS_RELATION_BUSINESS_SUBTYPE, businessSubType);
        return inputs;
    }
}
