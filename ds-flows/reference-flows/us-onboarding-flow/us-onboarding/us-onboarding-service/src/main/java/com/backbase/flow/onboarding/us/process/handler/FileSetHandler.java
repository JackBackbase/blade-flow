package com.backbase.flow.onboarding.us.process.handler;

import com.backbase.flow.documentstore.exceptions.FilesetAlreadyExistsException;
import com.backbase.flow.documentstore.service.DocumentStoreService;
import com.backbase.flow.iam.annotation.RunWithoutAuthorization;
import com.backbase.flow.process.ProcessConstants;
import com.backbase.flow.process.annotation.ProcessBean;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.mime.MediaType;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@ProcessBean("fileset")
public class FileSetHandler {

    private final DocumentStoreService documentStoreService;

    /**
     * <p>Creates a fileset for storing documents. It runs without requiring authorization.
     * It works by using creating a new Fileset and associating it to the Case Key. The
     * properties of the Fileset (name, max number of files, and allowed media types)
     * all come from the BPMN diagram.</p>
     *
     * @param execution Camunda's execution environment.
     */
    @RunWithoutAuthorization
    public void create(DelegateExecution execution, String fileSetName, Integer maxFiles, String allowedMediaTypes) {
        try {
            UUID caseKey = this.getCaseKey(execution);
            documentStoreService.createFileset(caseKey, fileSetName, maxFiles,
                this.getMediaTypes(allowedMediaTypes), Collections.emptySet());
        } catch (FilesetAlreadyExistsException e) {
            log.info(e.getMessage());
        }
    }

    /**
     * <p>Retrieves the Case Key from the current process by using Camunda's execution environment.
     * If no Case Key exists, it returns NULL. If the Key is found, then, it is converted to an
     * {@link UUID}.</p>
     *
     * @param execution Camunda's execution environment
     * @return a {@link UUID} with the current case's Key, or null if none exists.
     */
    private UUID getCaseKey(DelegateExecution execution) {
        Object caseKey = execution.getVariable(ProcessConstants.PROCESS_VARIABLE_CASE_KEY);
        return caseKey == null ? null : UUID.fromString(caseKey.toString());
    }

    /**
     * <p>It splits up the received string using commas as separators, and then maps it into
     * {@link MediaType} defined by Apache. These MediaTypes are then collected into a
     * {@link Set} that is used for the creation of the Fileset.</p>
     *
     * @param allowedMediaTypes a comma-separated {@link String} containing all the allowed
     *                          media types to upload to the Fileset.
     * @return a {@link Set} containing all the allowed {@link MediaType}
     */
    private Set<MediaType> getMediaTypes(String allowedMediaTypes) {
        return Arrays.stream(allowedMediaTypes.split(","))
            .map(MediaType::parse)
            .collect(Collectors.toSet());
    }
}
