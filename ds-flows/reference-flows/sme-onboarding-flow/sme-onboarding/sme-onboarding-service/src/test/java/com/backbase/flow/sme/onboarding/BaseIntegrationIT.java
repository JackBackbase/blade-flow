package com.backbase.flow.sme.onboarding;

import static com.backbase.flow.sme.onboarding.TestConstants.CASE_DEFINITION_ID;
import static org.apache.http.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.backbase.flow.casedata.CaseDataService;
import com.backbase.flow.casedata.CaseNotFoundException;
import com.backbase.flow.casedata.cases.Case;
import com.backbase.flow.casedata.cases.CaseRepository;
import com.backbase.flow.casedata.definition.CaseDefinitionId;
import com.backbase.flow.casedata.events.PersistedEvent;
import com.backbase.flow.casedata.events.PersistedEventFilter;
import com.backbase.flow.casedata.events.PersistedEventRepository;
import com.backbase.flow.casedata.mapper.JourneyMapper;
import com.backbase.flow.collections.api.model.CollectionResponseDto;
import com.backbase.flow.iam.FlowSecurityContext;
import com.backbase.flow.iam.auth.FlowSystemAuthentication;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.interaction.engine.data.model.InteractionDefinition;
import com.backbase.flow.interaction.engine.data.model.InteractionInstance;
import com.backbase.flow.interaction.engine.data.model.StepInstance;
import com.backbase.flow.interaction.engine.data.repository.InteractionDefinitionRepository;
import com.backbase.flow.interaction.engine.data.repository.InteractionInstanceRepository;
import com.backbase.flow.process.api.model.FlowJobDto;
import com.backbase.flow.process.api.model.FlowJobStatusDto;
import com.backbase.flow.process.service.FlowDecisionService;
import com.backbase.flow.process.service.FlowProcessService;
import com.backbase.flow.process.service.FlowTaskService;
import com.backbase.flow.service.businessrelations.ServiceItemBusinessRelationsProperties;
import com.backbase.flow.service.businessrelations.collection.RoleListProvider;
import com.backbase.flow.sme.onboarding.casedata.Address;
import com.backbase.flow.sme.onboarding.casedata.Applicant;
import com.backbase.flow.sme.onboarding.casedata.SmeCaseDefinition;
import com.backbase.flow.sme.onboarding.constants.ProcessConstants;
import com.backbase.flow.sme.onboarding.data.AmlBusinessApplicantJourneyReader;
import com.backbase.flow.sme.onboarding.data.AmlBusinessInfoJourneyReader;
import com.backbase.flow.sme.onboarding.data.AmlBusinessInfoJourneyWriter;
import com.backbase.flow.sme.onboarding.data.AmlPersonApplicantJourneyReader;
import com.backbase.flow.sme.onboarding.data.AmlPersonInfoJourneyReader;
import com.backbase.flow.sme.onboarding.data.AmlPersonInfoJourneyWriter;
import com.backbase.flow.sme.onboarding.data.BusinessAddressJourneyReader;
import com.backbase.flow.sme.onboarding.data.BusinessAddressJourneyWriter;
import com.backbase.flow.sme.onboarding.data.DocumentRequestDataReader;
import com.backbase.flow.sme.onboarding.data.DocumentUpdateDataWriter;
import com.backbase.flow.sme.onboarding.data.OtpJourneyReader;
import com.backbase.flow.sme.onboarding.data.OtpJourneyWriter;
import com.backbase.flow.sme.onboarding.data.PersonalAddressJourneyReader;
import com.backbase.flow.sme.onboarding.data.PersonalAddressJourneyWriter;
import com.backbase.flow.sme.onboarding.interaction.hook.CompleteAddressValidationIdtHook;
import com.backbase.flow.sme.onboarding.interaction.hook.CompleteIdvIdtHook;
import com.backbase.flow.validation.definition.ValidationResult;
import com.backbase.flow.validation.util.ValidationUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class BaseIntegrationIT {

    static {
        System.setProperty("SIG_SECRET_KEY", "JWTSecretKeyDontUseInProduction!");
    }

    protected static final String AUTHORIZATION_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9" +
        ".eyJzdWIiOiJtYW5hZ2VyIiwiY25leHAiOnRydWUsImFubG9jIjp0cnVlLCJhbmV4cCI6dHJ1ZSwiZW5ibCI6dHJ1ZSwicm9sIjpbIlJPTEVfMSIsIlJPTEVfMiJdfQ.JU15oPlGsq71QB11wiKLsK9tZ3goDr3zpl5eXf1MltY";

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected FlowSecurityContext flowSecurityContext;
    @Autowired
    protected CaseDataService caseDataService;
    @Autowired
    protected PersistedEventRepository persistedEventRepository;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected InteractionDefinitionRepository interactionDefinitionRepository;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected FlowTaskService flowTaskService;
    @Autowired
    protected InteractionInstanceRepository interactionInstanceRepository;
    @Autowired
    protected FlowDecisionService flowDecisionService;
    @Autowired
    protected FlowProcessService flowProcessService;
    @Autowired
    protected DocumentRequestDataReader documentRequestDataReader;
    @Autowired
    protected DocumentUpdateDataWriter documentUpdateDataWriter;
    @Autowired
    protected BusinessAddressJourneyReader businessAddressJourneyReader;
    @Autowired
    protected BusinessAddressJourneyWriter businessAddressJourneyWriter;
    @Autowired
    protected PersonalAddressJourneyReader personalAddressJourneyReader;
    @Autowired
    protected PersonalAddressJourneyWriter personalAddressJourneyWriter;
    @Autowired
    protected AmlBusinessApplicantJourneyReader amlBusinessApplicantJourneyReader;
    @Autowired
    protected AmlBusinessInfoJourneyReader amlBusinessInfoJourneyReader;
    @Autowired
    protected AmlBusinessInfoJourneyWriter amlBusinessInfoJourneyWriter;
    @Autowired
    protected AmlPersonApplicantJourneyReader amlPersonApplicantJourneyReader;
    @Autowired
    protected AmlPersonInfoJourneyReader amlPersonInfoJourneyReader;
    @Autowired
    protected AmlPersonInfoJourneyWriter amlPersonInfoJourneyWriter;
    @Autowired
    protected OtpJourneyReader otpJourneyReader;
    @Autowired
    protected OtpJourneyWriter otpJourneyWriter;
    @Autowired
    protected CaseRepository caseRepository;
    @Autowired
    protected HistoryService historyService;
    @Autowired
    protected ValidationUtil validationUtil;
    @Autowired
    protected CompleteAddressValidationIdtHook completeAddressValidationIdtHook;
    @Autowired
    protected CompleteIdvIdtHook completeIdvIdtHook;
    @Autowired
    protected RoleListProvider roleListProvider;
    @Autowired
    @Qualifier("advJourneyMapper")
    protected JourneyMapper<Map> addressValidationJourneyMapper;
    @Autowired
    protected ServiceItemBusinessRelationsProperties serviceItemBusinessRelationsProperties;

    private InteractionDefinition smeOnboardingInteractionDefinition;
    private InteractionDefinition smeApplicationCenterInteractionDefinition;

    @PostConstruct
    private void setup() {
        this.smeOnboardingInteractionDefinition = getInteractionDefinition(TestConstants.SME_ONBOARDING);
        this.smeApplicationCenterInteractionDefinition =
            getInteractionDefinition(TestConstants.SME_APPLICATION_CENTER);
    }

    protected Case startPreliminaryCase(SmeCaseDefinition caseDefinition) {
        return getWithoutAuthorization(() -> caseDataService
            .startPreliminaryCase(new CaseDefinitionId(CASE_DEFINITION_ID), caseDefinition));
    }

    protected Case startCase(SmeCaseDefinition caseDefinition) {
        return getWithoutAuthorization(() -> caseDataService
            .startCase(new CaseDefinitionId(CASE_DEFINITION_ID), caseDefinition));
    }

    protected SmeCaseDefinition getCaseData(UUID caseKey) {
        return caseRepository.findByKey(caseKey).orElseThrow(() -> new CaseNotFoundException(caseKey))
            .getCaseData(SmeCaseDefinition.class);
    }

    protected InteractionInstance interactionInstanceFromCase(String interactionStepName, String userId, Case smeCase) {
        var interactionInstanceToSave = new InteractionInstance();
        interactionInstanceToSave.setCaseKey(smeCase.getKey());
        interactionInstanceToSave.setInteractionDefinition(smeOnboardingInteractionDefinition);
        interactionInstanceToSave.setId(UUID.randomUUID());
        interactionInstanceToSave.setUserId(userId);
        interactionInstanceToSave.setCurrentStep(new StepInstance(interactionStepName));
        return interactionInstanceRepository.save(interactionInstanceToSave);
    }

    protected InteractionInstance createApplicationCenterInteractionInstanceInCaseStore(
        SmeCaseDefinition caseDefinition,
        String interactionStepName, String userId
    ) {
        var smeCase = startCase(caseDefinition);
        return applicationCenterInteractionInstanceFromCase(interactionStepName, userId, smeCase);
    }

    protected InteractionInstance applicationCenterInteractionInstanceFromCase(
        String interactionStepName, String userId, Case smeCase
    ) {
        var interactionInstanceToSave = new InteractionInstance();
        interactionInstanceToSave.setCaseKey(smeCase.getKey());
        interactionInstanceToSave.setInteractionDefinition(smeApplicationCenterInteractionDefinition);
        interactionInstanceToSave.setId(UUID.randomUUID());
        interactionInstanceToSave.setUserId(userId);
        interactionInstanceToSave.setCurrentStep(new StepInstance(interactionStepName));
        return interactionInstanceRepository.save(interactionInstanceToSave);
    }

    protected InteractionInstance createInteractionInstance(
        SmeCaseDefinition caseDefinition, String interactionStepName, String userId
    ) {
        var smeCase = startPreliminaryCase(caseDefinition);
        return interactionInstanceFromCase(interactionStepName, userId, smeCase);
    }

    protected InteractionInstance createInteractionInstanceInCaseStore(
        SmeCaseDefinition caseDefinition, String interactionStepName, String userId
    ) {
        var smeCase = startCase(caseDefinition);
        return interactionInstanceFromCase(interactionStepName, userId, smeCase);
    }

    protected Case getCase(String caseKey) {
        return getCase(UUID.fromString(caseKey));
    }

    protected Case getCase(UUID caseKey) {
        var optionalCase = caseRepository.findByKey(caseKey);
        return optionalCase.orElse(null);
    }

    protected List<PersistedEvent> getEventsByClass(UUID caseKey, Class<?> clazz) {
        return flowSecurityContext.runWithoutAuthorization(() ->
            persistedEventRepository.filterEvents(caseKey, new PersistedEventFilter()).stream()
                .filter(event -> event.getPayloadClassName().equals(clazz.getName()))
                .collect(Collectors.toList()));
    }

    protected List<PersistedEvent> getEventsByName(UUID caseKey, String eventName) {
        return persistedEventRepository.filterEvents(caseKey, new PersistedEventFilter()).stream()
            .filter(event -> event.getEventName().equals(eventName))
            .collect(Collectors.toList());
    }

    protected boolean containsAllEvents(UUID caseKey, List<String> eventNames) {
        return persistedEventRepository.filterEvents(caseKey, new PersistedEventFilter())
            .stream()
            .map(PersistedEvent::getEventName)
            .collect(Collectors.toList())
            .containsAll(eventNames);
    }

    protected boolean containsEvent(UUID caseKey, String eventName) {
        return containsAllEvents(caseKey, List.of(eventName));
    }

    protected void completeUserTask(String caseKey, String processDefinitionKey, String taskDefinitionKey) {
        var taskId = taskService.createTaskQuery()
            .processVariableValueEquals(ProcessConstants.PROCESS_VARIABLE_CASE_KEY, caseKey)
            .processDefinitionKey(processDefinitionKey)
            .taskDefinitionKey(taskDefinitionKey)
            .singleResult()
            .getId();

        flowTaskService.completeTask(taskId, Collections.emptyMap(), null);
    }

    protected void checkInteractionResponse(InteractionResponseDto interactionResponseDto, String expectedStep) {
        assertSoftly(softly -> {
            softly.assertThat(interactionResponseDto.getInteractionId()).isNotBlank();
            softly.assertThat(interactionResponseDto.getActionErrors()).isEmpty();
            softly.assertThat(interactionResponseDto.getStep().getName()).isEqualTo(expectedStep);
        });
    }

    @SneakyThrows
    protected InteractionResponseDto interaction(
        String interactionUrl, String action, String interactionId, Object payload, String mediaType,
        InputStream inputStream
    ) {
        var fileName = "myBusinessLicense";
        var interactionRequest = new InteractionRequestDto()
            .interactionId(interactionId)
            .payload(payload);

        MockMultipartFile interactionPart = new MockMultipartFile("interaction", "",
            APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsBytes(interactionRequest));
        MockMultipartFile filePart = new MockMultipartFile("files", fileName, mediaType, inputStream);

        var response = mockMvc.perform(multipart(TestConstants.FILE_URL, interactionUrl, action)
                .file(interactionPart)
                .file(filePart)
                .header(AUTHORIZATION, AUTHORIZATION_TOKEN)
                .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

        return jsonToObject(response, InteractionResponseDto.class);
    }

    @SneakyThrows
    protected InteractionResponseDto interaction(
        String interactionUrl, String action, String interactionId, Object payload
    ) {
        var interactionRequest = new InteractionRequestDto()
            .interactionId(interactionId)
            .payload(payload);

        var response = mockMvc.perform(
                post(TestConstants.ACTION_URL, interactionUrl, action)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, AUTHORIZATION_TOKEN)
                    .content(objectMapper.writeValueAsString(interactionRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

        return jsonToObject(response, InteractionResponseDto.class);
    }

    protected <T extends Serializable> List<T> getCollectionItems(String collectionName, Class<T> itemClass) {
        var collectionResponseDto = getCollection(collectionName);
        return castToList(collectionResponseDto.getItems(), itemClass);
    }

    @SneakyThrows
    protected CollectionResponseDto getCollection(String collectionName) {
        var response =
            mockMvc.perform(get("/client-api/collections/v2/collections/{collectionName}?count=true", collectionName)
                    .contentType(APPLICATION_JSON)
                    .header(AUTHORIZATION, AUTHORIZATION_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return jsonToObject(response, CollectionResponseDto.class);
    }

    protected <T> T getWithoutAuthorization(Callable<T> callable) {
        setSystemAuthenticationContext();
        return flowSecurityContext.runWithoutAuthorization(callable);
    }

    protected void runWithoutAuthorization(Runnable runnable) {
        setSystemAuthenticationContext();
        flowSecurityContext.runWithoutAuthorization(runnable);
    }

    protected void setSystemAuthenticationContext() {
        SecurityContextHolder.setContext(new SecurityContextImpl(new FlowSystemAuthentication()));
    }

    @SneakyThrows
    protected <T> T jsonToObject(String json, Class<T> objectClass) {
        return objectMapper.readValue(json, objectClass);
    }

    @SneakyThrows
    protected <T> T mapToObject(Map<String, Object> map, Class<T> objectClass) {
        return objectMapper.convertValue(map, objectClass);
    }

    protected <T> T mapToObject(Object object, Class<T> objectClass) {
        return mapToObject((Map<String, Object>) object, objectClass);
    }

    protected <T> List<T> mapToList(Object object, Class<T> elementClass) {
        return ((List<?>) object).stream()
            .map(element -> mapToObject(element, elementClass))
            .collect(Collectors.toUnmodifiableList());
    }

    protected <T> List<T> castToList(Object object, Class<T> elementClass) {
        return ((List<?>) object).stream()
            .map(elementClass::cast)
            .collect(Collectors.toUnmodifiableList());
    }

    protected void checkError(
        InteractionResponseDto interactionResponseDto, String expectedStep, String... expectedFieldsWithError
    ) {
        assertSoftly(softly -> {
            softly.assertThat(interactionResponseDto.getInteractionId()).isNotBlank();
            softly.assertThat(interactionResponseDto.getStep().getName()).isEqualTo(expectedStep);
            var fieldsWithError = interactionResponseDto.getActionErrors()
                .stream()
                .map(modelError -> modelError.getContext().keySet())
                .flatMap(Set::stream)
                .collect(Collectors.toUnmodifiableSet());
            softly.assertThat(fieldsWithError).containsExactlyInAnyOrder(expectedFieldsWithError);
        });
    }

    protected Applicant getMainApplicant(UUID id) {
        return new Applicant()
            .withId(id.toString())
            .withFirstName("Kylie")
            .withLastName("Minogue")
            .withDateOfBirth(LocalDate.now().minusYears(20))
            .withEmail("kylie@backbase.com")
            .withRelationType(Applicant.RelationType.OWNER)
            .withRole("CEO")
            .withControlPerson(true)
            .withIsRegistrar(true)
            .withOwnershipPercentage(30.0)
            .withPersonalAddress(new Address()
                .withCity("Bytom")
                .withState("SY")
                .withZipCode("41923")
                .withApt("26")
                .withNumberAndStreet("Daleka"));
    }

    protected void checkValidationError(Object payload, String fieldName, String key, String message) {
        assertThat(fieldErrors(payload, fieldName))
            .anyMatch(error -> key.equals(error.getKey()) && Map.of(fieldName, message).equals(error.getContext()));
    }

    protected void checkNoValidationError(Object payload, String fieldName) {
        assertThat(fieldErrors(payload, fieldName)).isEmpty();
    }

    protected List<Error> fieldErrors(Object payload, String fieldName) {
        return validationUtil.validate(payload).stream()
            .filter(result -> fieldName.equals(result.getFieldName()))
            .map(ValidationResult::getError)
            .collect(Collectors.toUnmodifiableList());
    }

    protected InteractionDefinition getInteractionDefinition(String key) {
        return interactionDefinitionRepository.findFirstByIdKeyStartingWithOrderByIdDesc(key).orElseThrow();
    }

    @SneakyThrows
    protected String openTask(String taskId) {
        var responseBody = mockMvc
            .perform(post("/client-api/process/v2/task/" + taskId + "/open")
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, AUTHORIZATION_TOKEN))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

        return objectMapper.readValue(responseBody, new TypeReference<FlowJobDto>() {
        }).getJobId();
    }

    @SneakyThrows
    protected String getInteractionIdFromTaskAndJob(String idtTaskId, String jobId) {
        var responseBody = mockMvc
            .perform(get("/client-api/process/v2/task/" + idtTaskId + "/job/" + jobId)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, AUTHORIZATION_TOKEN))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

        return objectMapper.readValue(responseBody, new TypeReference<FlowJobStatusDto>() {
        }).getResourceId();
    }

    @SneakyThrows
    protected void completeUserTask(String taskId, Object taskData) {
        mockMvc.perform(post("/client-api/process/v2/task/" + taskId + "/complete")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("taskData", taskData)))
                .header(AUTHORIZATION, AUTHORIZATION_TOKEN)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isAccepted());
    }

    @SneakyThrows
    protected void getAndSaveBusinessDocument(UUID caseKey, String filesetName, String filerefName) {
        final var url = "/client-api/casedata/v3/cases/{caseKey}/filesets/{filesetName}/filerefs/{filerefName}/raw";
        final var response = mockMvc.perform(
                get(url, caseKey, filesetName, filerefName)
                    .contentType(APPLICATION_JSON)
                    .header(AUTHORIZATION, AUTHORIZATION_TOKEN)
                    .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsByteArray();

        Files.write(Paths.get("target", filerefName), response);
    }
}
