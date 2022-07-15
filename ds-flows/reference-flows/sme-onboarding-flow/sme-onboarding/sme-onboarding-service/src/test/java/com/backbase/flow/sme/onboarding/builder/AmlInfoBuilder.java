package com.backbase.flow.sme.onboarding.builder;

import com.backbase.flow.service.aml.casedata.AntiMoneyLaunderingInfo;
import com.backbase.flow.sme.onboarding.casedata.AmlInfo;
import com.backbase.flow.sme.onboarding.casedata.AmlResult;
import com.backbase.flow.sme.onboarding.casedata.ReviewApprovedReason;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AmlInfoBuilder {

    private boolean reviewNeeded;
    private boolean reviewApproved;
    private String reviewApprovedComment;
    private String reviewDeclinedComment;
    private AntiMoneyLaunderingInfo.Status dtoStatus;
    private AmlInfo.Status caseStatus;
    private String requestDate;
    private String matchStatus;
    private String shareUrl;

    public static AmlInfoBuilder getInstance() {
        return new AmlInfoBuilder();
    }

    public AmlInfoBuilder reviewNeeded(boolean reviewNeeded) {
        this.reviewNeeded = reviewNeeded;
        return this;
    }

    public AmlInfoBuilder reviewApproved(boolean reviewApproved) {
        this.reviewApproved = reviewApproved;
        return this;
    }

    public AmlInfoBuilder reviewDeclinedComment(String reviewDeclinedComment) {
        this.reviewDeclinedComment = reviewDeclinedComment;
        return this;
    }

    public AmlInfoBuilder reviewApprovedComment(String reviewApprovedComment) {
        this.reviewApprovedComment = reviewApprovedComment;
        return this;
    }

    public AmlInfoBuilder dtoStatus(AntiMoneyLaunderingInfo.Status dtoStatus) {
        this.dtoStatus = dtoStatus;
        return this;
    }

    public AmlInfoBuilder caseStatus(AmlInfo.Status caseStatus) {
        this.caseStatus = caseStatus;
        return this;
    }

    public AmlInfoBuilder requestDate(String requestDate) {
        this.requestDate = requestDate;
        return this;
    }

    public AmlInfoBuilder matchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
        return this;
    }

    public AmlInfoBuilder shareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
        return this;
    }

    public AmlInfo buildAmlInfo() {
        var amlInfo = new AmlInfo();

        amlInfo.setReviewNeeded(reviewNeeded);
        amlInfo.setReviewApproved(reviewApproved);
        amlInfo.setReviewDeclinedComment(reviewDeclinedComment);

        amlInfo.setReviewApprovedReason(new ReviewApprovedReason());
        amlInfo.getReviewApprovedReason().setComment(reviewApprovedComment);

        amlInfo.setStatus(caseStatus);

        amlInfo.setAmlResult(new AmlResult());
        amlInfo.getAmlResult().setRequestDate(requestDate);
        amlInfo.getAmlResult().setMatchStatus(matchStatus);
        amlInfo.getAmlResult().setShareUrl(shareUrl);

        return amlInfo;
    }

    public AntiMoneyLaunderingInfo buildAntiMoneyLaunderingInfo() {
        var amlInfo = new AntiMoneyLaunderingInfo();

        amlInfo.setReviewNeeded(reviewNeeded);
        amlInfo.setReviewApproved(reviewApproved);
        amlInfo.setReviewDeclinedComment(reviewDeclinedComment);

        amlInfo.setReviewApprovedReason(new com.backbase.flow.service.aml.casedata.ReviewApprovedReason());
        amlInfo.getReviewApprovedReason().setComment(reviewApprovedComment);

        amlInfo.setStatus(dtoStatus);

        amlInfo.setAmlResult(new com.backbase.flow.service.aml.casedata.AmlResult());
        amlInfo.getAmlResult().setRequestDate(requestDate);
        amlInfo.getAmlResult().setMatchStatus(matchStatus);
        amlInfo.getAmlResult().setShareUrl(shareUrl);

        return amlInfo;
    }
}
