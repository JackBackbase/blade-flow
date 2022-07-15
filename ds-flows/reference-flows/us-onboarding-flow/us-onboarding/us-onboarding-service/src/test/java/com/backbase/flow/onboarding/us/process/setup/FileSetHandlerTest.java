package com.backbase.flow.onboarding.us.process.setup;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.backbase.flow.documentstore.FilesetView;
import com.backbase.flow.documentstore.service.DocumentStoreService;
import com.backbase.flow.onboarding.us.process.handler.FileSetHandler;
import org.apache.tika.mime.MediaType;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;

class FileSetHandlerTest {

    private final DocumentStoreService documentStoreService = mock(DocumentStoreService.class);
    private final DelegateExecution execution = mock(DelegateExecution.class);
    private final FileSetHandler fileSetHandler = new FileSetHandler(documentStoreService);

    @Test
    void create_createsAFilesetSuccessfully() {
        final FilesetView filesetView = FilesetView.builder()
            .name("Test")
            .maxFiles(5)
            .allowedMediaType(MediaType.TEXT_PLAIN)
            .build();
        when(documentStoreService.createFileset(any(), any(), any(), any(), any())).thenReturn(filesetView);

        fileSetHandler.create(execution, "Test", 5, "");

        verify(documentStoreService, times(1)).createFileset(any(), any(), any(), any(), any());
    }
}