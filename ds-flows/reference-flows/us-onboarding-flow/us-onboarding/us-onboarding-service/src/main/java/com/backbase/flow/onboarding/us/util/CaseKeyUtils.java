package com.backbase.flow.onboarding.us.util;

import com.backbase.buildingblocks.presentation.errors.NotFoundException;
import com.backbase.flow.process.ProcessConstants;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CaseKeyUtils {

    /**
     * Get the case key from process variable.
     *
     * @param execution The execution from which to get the case key.
     * @return The case key.
     * @throws NotFoundException If no case key is found.
     */
    public static UUID getCaseKey(DelegateExecution execution) {
        Object caseKey = execution.getVariable(ProcessConstants.PROCESS_VARIABLE_CASE_KEY);
        if (Objects.isNull(caseKey)) {
            String message = "No case key has been found.";
            log.error(message);
            throw new NotFoundException(message);
        }
        return UUID.fromString(caseKey.toString());
    }
}
