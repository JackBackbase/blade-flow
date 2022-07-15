package com.backbase.flow.onboarding.us.validation;

import lombok.experimental.UtilityClass;

/**
 * Constants class for all regular expression patterns and messages used in the javax validations. Please follow the
 * naming convention when adding constants: Regex constants: suffix these with _REGEX_PATTERN Message constants: suffix
 * these with _MESSAGE This will make it clearer what they are when importing from other classes.
 */
@UtilityClass
public class ValidationConstants {

    // Validation regular expression patterns
    /**
     * A valid Social Security Number cannot: • Contain all zeroes in any specific group (e.g 000-##-####, ###-00-####,
     * or ###-##-0000) • Begin with ‘666’. • Begin with any value from ‘900-999′ • Be ‘078-05-1120′ (due to the
     * Woolworth’s Wallet Fiasco) • Be ‘219-09-9999′ (as it appeared in an advertisement for the Social Security
     * Administration)
     */
    public static final String SSN_REGEX_PATTERN =
        "^(?!219099999|078051120)(?!666|000|9\\d{2})\\d{3}(?!00)\\d{2}(?!0{4})\\d{4}$";
    public static final String EMAIL_REGEX_PATTERN =
        "^([ ]*[A-Za-z0-9]+[-A-Za-z0-9+.#_]*)@([A-Za-z0-9]+[-A-Za-z0-9#_]*)\\.([A-Za-z0-9\\-]+[.]?[A-Za-z0-9]+[ ]*)$";
    public static final String PHONE_REGEX_PATTERN = "^\\+[1-9]\\d{5,14}$";
    // Validation messages
    public static final String FIELD_LENGTH_MESSAGE = "Field length may not exceed {max} characters.";
    public static final String PSW_NULL_MESSAGE = "Insert your password.";
    public static final String PSW_LENGTH_8_MESSAGE = "Password should contain at least 8 characters.";
    public static final String PSW_ONE_DIGIT_MESSAGE = "Password should include at least one digit.";
    public static final String PSW_ONE_UPPERCASE_MESSAGE = "Password should include at least one "
        + "uppercase letter.";
    public static final String EMAIL_NULL_MESSAGE = "Insert your email.";
    public static final String PHONE_NUMBER_NULL_MESSAGE = "Insert your phoneNumber.";
    public static final String WITHHOLDING_TAX_ACCEPTED_NULL_MESSAGE = "Provide your witholding tax decision";
    public static final String W8BEN_ACCEPTED_NULL_MESSAGE = "Provide your W8-BEN decision";
    public static final String RESIDENCY_ADDRESS_NULL_MESSAGE = "Provide your residency address";
    public static final String CITIZENSHIP_COUNTRY_CODE_NULL_MESSAGE = "Provide your residency citizenship country code";
    public static final String RESIDENCY_COUNTRY_CODE_BLANK_MESSAGE = "Provide your residency country code";
    public static final String RESIDENCY_NUMBER_AND_STREET_BLANK_MESSAGE = "Provide your residency number and street";
    public static final String RESIDENCY_CITY_BLANK_MESSAGE = "Provide your residency city";
    public static final String RESIDENCY_ZIP_CODE_BLANK_MESSAGE = "Provide your residency zip code";
    public static final String JOINT_ACCOUNT_NULL_MESSAGE = "Provide your joint account choice";
    public static final String APPROVED_NULL_MESSAGE = "Provide your task approval choice";
    public static final String CITIZENSHIP_NULL_MESSAGE = "Provide your citizenship type choice";
    public static final String EMAIL_VALIDATION_MESSAGE = "Email address must be valid.";
    public static final String PHONE_VALIDATION_MESSAGE = "Phone number must be valid.";
    public static final String SSN_VALIDATION_MESSAGE = "Please provide a valid SSN.";
    public static final String INVALID_ENUM_MESSAGE = "Invalid enum value. Please provide a valid enum.";

}