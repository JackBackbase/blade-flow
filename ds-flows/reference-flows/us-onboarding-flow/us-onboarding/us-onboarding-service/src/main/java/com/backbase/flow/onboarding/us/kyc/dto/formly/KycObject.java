package com.backbase.flow.onboarding.us.kyc.dto.formly;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "key",
        "templateOptions",
        "validators"
})
public class KycObject {

    @JsonProperty("type")
    private String type;
    @JsonProperty("key")
    private String key;
    @JsonProperty("templateOptions")
    private TemplateOptions templateOptions;
    @JsonProperty("validators")
    private Validators validators;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("templateOptions")
    public TemplateOptions getTemplateOptions() {
        return templateOptions;
    }

    @JsonProperty("templateOptions")
    public void setTemplateOptions(TemplateOptions templateOptions) {
        this.templateOptions = templateOptions;
    }

    @JsonProperty("validators")
    public Validators getValidators() {
        return validators;
    }

    @JsonProperty("validators")
    public void setValidators(Validators validators) {
        this.validators = validators;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}