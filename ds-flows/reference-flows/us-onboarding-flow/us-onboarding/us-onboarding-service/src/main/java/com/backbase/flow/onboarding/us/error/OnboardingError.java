package com.backbase.flow.onboarding.us.error;

import com.backbase.buildingblocks.presentation.errors.Error;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public enum OnboardingError {

    ANCHOR_DATA_FAILED("ONB_001", "Anchor data submission failed."),
    RESUME_FINALIZE_FAILED("ONB_003", "Unable to finalize onboarding.");

    private final Error error = new Error();

    OnboardingError(final String key, final String message) {
        this.error.setKey(key);
        this.error.setMessage(message);
    }

    /**
     * <p>Gets a mutable {@link Error} class to be used on {@link #getErrorWithContext(String, String)}.</p>
     * @return the mutable version of {@code this.error}
     */
    public Error getError() {
        final Error errorCopy = new Error();
        errorCopy.setKey(this.error.getKey());
        errorCopy.setMessage(this.error.getMessage());
        return errorCopy;
    }

    /**
     * <p>Sets the context to a mutable version of {@code this.error} and then returns that version.</p>
     * @param key the key to be used on the error
     * @param message the message to be used on the error
     * @return an {@link Error} with key, message, and context set
     */
    public Error getErrorWithContext(final String key, final String message) {
        final Map<String, String> context = ImmutableMap.of(key, message);
        final Error errorCopy = getError();
        errorCopy.setContext(context);
        return errorCopy;
    }

}
