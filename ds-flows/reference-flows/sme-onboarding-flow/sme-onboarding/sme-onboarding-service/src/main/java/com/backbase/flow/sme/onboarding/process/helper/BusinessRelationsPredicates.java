package com.backbase.flow.sme.onboarding.process.helper;

import static com.backbase.flow.sme.onboarding.casedata.BusinessRelationsCaseData.Status;

import java.util.function.Predicate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BusinessRelationsPredicates {

    public static final Predicate<Status> hasCompleteStatus = status -> status.equals(Status.COMPLETE);
    public static final Predicate<Status> hasInReviewStatus = status -> status.equals(Status.IN_REVIEW);

    public static Predicate<Status> isDataProvided() {
        return hasCompleteStatus.or(hasInReviewStatus);
    }
}
