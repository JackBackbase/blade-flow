package com.backbase.flow.onboarding.us;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.backbase.flow.casedata.cases.CaseRepository;
import com.backbase.flow.casedata.cases.indexing.IndexedCasePropertyRepository;
import com.backbase.flow.casedata.status.CaseStatusRepository;
import com.backbase.flow.interaction.api.model.InteractionRequestDto;
import com.backbase.flow.interaction.api.model.InteractionResponseDto;
import com.backbase.flow.interaction.engine.data.model.InteractionInstance;
import com.backbase.flow.interaction.engine.data.repository.InteractionInstanceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import org.camunda.bpm.engine.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIT {


    private static final String INTERACTIONS_ROOT = "/client-api/interaction/v2/interactions";
    private static final String ACTION_ENDPOINT = "/actions/{actionName}";
    private static final String INTERACTION = "onboarding";

    private static String actionPath = INTERACTIONS_ROOT + "/" + INTERACTION + "/actions/{actionName}";
    private static String anonymousUserId = "some-user-id";
    private static ObjectMapper objectMapper;
    private static MockMvc mockMvc;
    private static Cookie anonymousCookie;

    private static final String TOKEN_FOR_USER = "Bearer eyJraWQiOiJNYmV1VmVVWlhVT2FJcDgwYmx1XC9sanFOQjNKZE9aSDgxQ3JGU0tpMmVcL2M9IiwiY3R5IjoiSldUIiwiZW5jIjoiQTEyOENCQy1IUzI1NiIsImFsZyI6ImRpciJ9..agl2C8OHUf9RxV9HzCo6bQ.6HohLAs1T7WHoHRGcQRLif4QAtT3vVvnq5PhltBgRH8TGJ8hd81qGDjh-PTkjd5jlKjL7dapt6Z6-GQIM1U-8che8dSCip7qlbFH7ahjvj8_1CYwfltvbuZGITDFPyNC7Z0aVitSoPPlbG9deUkUt9i1hiS6wVJswYkHT4mW9C08wJKIpUUbLXvCLei0H1xNBFGhGixYl1NbLtaHDxxuNjK_VF759mspLM-q0MKU7IhWjxnlYBZWjACU7IANxbxVZZMkeVFuCHc6C_SPKL_zRCICxQ8Gc9vHR7jRBCsZoSzhF770DnPZw2MNcQ3wUF7gCFA1Qq-NGUTK9zWjrboW62NGsN9BR9FmM1iDnqvwKJvS9ZiVCHX2Qt6p-0pud7-rwFQqb11K880Bea5kd6mNGhyhqSmfyKZ2rc1XfrBu98dIZkiWGgW9ALQokh_-GTWen2HzPeeJNhugJMWLY2tagTKcL5eLtYsYvH4PJp16J51DTRx-H2_GT-Fl-ahB0PgCw2d5wBLxajgB7vikKoYNlAdeBBh56z_1yprTIsDDpqWRfLI1-tAGGvy-MZrrI-1G.5VIZJWMAwJGjtQx2VTgwnA";

    public static boolean isIDVMocked;

    static {
        System.setProperty("SIG_SECRET_KEY", "JWTSecretKeyDontUseInProduction!");
    }

    @Autowired
    protected AbstractApplicationContext springContext;
    @Autowired
    protected MockMvc autowiredMockMvc;
    @Autowired
    protected CaseRepository caseRepository;
    @Autowired
    protected InteractionInstanceRepository interactionInstanceRepository;
    @Autowired
    protected CaseStatusRepository caseStatusRepository;
    @Autowired
    TaskService taskService;
    @Value("${backbase.flow.iam.anonymous.cookie}")
    private String anonymousUserCookieKey;
    @Autowired
    private ObjectMapper autowiredObjectMapper;
    @Autowired
    private IndexedCasePropertyRepository indexedCasePropertyRepository;

    static void setInteraction(String interaction) {
        actionPath = INTERACTIONS_ROOT + "/" + interaction + ACTION_ENDPOINT;
    }

    protected static InteractionResponseDto performActionWithJwt(String action, InteractionRequestDto request)
        throws Exception {
        final String contentAsString = mockMvc.perform(post(actionPath, action)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .cookie(anonymousCookie)
            .header("Authorization", TOKEN_FOR_USER)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        final InteractionResponseDto result = objectMapper.readValue(contentAsString, InteractionResponseDto.class);
        assertThat(result.getInteractionId()).isNotBlank();

        return result;
    }

    protected static InteractionResponseDto performAction(String action, InteractionRequestDto request)
        throws Exception {
        final String contentAsString = mockMvc.perform(post(actionPath, action)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .cookie(anonymousCookie)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        final InteractionResponseDto result = objectMapper.readValue(contentAsString, InteractionResponseDto.class);
        assertThat(result.getInteractionId()).isNotBlank();

        return result;
    }

    @PostConstruct
    public void init() {
        objectMapper = autowiredObjectMapper;
        mockMvc = autowiredMockMvc;
        anonymousCookie = new Cookie(anonymousUserCookieKey, anonymousUserId);
        isIDVMocked = springContext.containsBeanDefinition("idvMockConnector");
    }

    @BeforeEach
    public void clear() {
        interactionInstanceRepository.deleteAll();
        indexedCasePropertyRepository.deleteAll();
        caseStatusRepository.deleteAll();
        caseRepository.deleteAll();
    }

    void randomizeAnonymousIdAndCookie() {
        anonymousUserId = UUID.randomUUID().toString();
        anonymousCookie = new Cookie(anonymousUserCookieKey, anonymousUserCookieKey);
    }


    String getCaseId(UUID interactionId) {
        Optional<InteractionInstance> interactionOptional = interactionInstanceRepository
            .findById(interactionId);

        UUID caseKey = interactionOptional.map(InteractionInstance::getCaseKey).orElse(null);

        return caseKey != null ? caseKey.toString() : null;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }


    public static MockMvc getMockMvc() {
        return mockMvc;
    }
}
