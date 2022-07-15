package com.backbase.flow.onboarding.us.service;

import com.backbase.flow.process.ProcessConstants;
import com.backbase.flow.process.service.FlowTaskService;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskCompletionService {

    private final TaskService taskService;
    private final FlowTaskService flowTaskService;

    public void completeTaskByKeyForCaseKey(String taskDefinitionKey, UUID caseKey) {
        List<Task> tasks = taskService.createTaskQuery()
            .active()
            .taskDefinitionKey(taskDefinitionKey)
            .processVariableValueEquals(ProcessConstants.PROCESS_VARIABLE_CASE_KEY, caseKey.toString())
            .list();
        for (Task task : tasks) {
            flowTaskService.completeTask(task.getId(), Collections.emptyMap(), null);
        }
    }
}
