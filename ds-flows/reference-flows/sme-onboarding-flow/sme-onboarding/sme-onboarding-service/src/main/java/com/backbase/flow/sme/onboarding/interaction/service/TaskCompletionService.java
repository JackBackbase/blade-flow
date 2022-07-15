package com.backbase.flow.sme.onboarding.interaction.service;

import com.backbase.flow.process.ProcessConstants;
import com.backbase.flow.process.service.FlowTaskService;
import com.backbase.flow.sme.onboarding.interaction.model.TaskDto;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskCompletionService {

    private final FlowTaskService flowTaskService;
    private final TaskService taskService;

    public void completeTaskAssociatedToApplicant(String taskDefinitionKey, UUID caseKey,
                                                  Predicate<TaskDto> taskReadyToComplete) {
        taskService.createTaskQuery()
            .active()
            .taskDefinitionKey(taskDefinitionKey)
            .processVariableValueEquals(ProcessConstants.PROCESS_VARIABLE_CASE_KEY, caseKey.toString())
            .list()
            .stream()
            .map(task -> TaskDto.builder()
                .taskId(task.getId())
                .applicantId(getAssociatedApplicantId(task))
                .build())
            .filter(taskReadyToComplete)
            .forEach(task -> flowTaskService.completeTask(task.getTaskId(), Collections.emptyMap(), null));
    }

    private String getAssociatedApplicantId(Task task) {
        return String.valueOf(taskService.getVariable(task.getId(), "applicantId"));
    }
}
