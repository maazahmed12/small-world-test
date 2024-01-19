package com.smallworld.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    private long mtn;

    private double amount;

    private String senderFullName;

    private int senderAge;

    private String beneficiaryFullName;

    private int beneficiaryAge;

    private List<Issue> issues;

    public long getMtn() {
        return mtn;
    }

    public void setMtn(long mtn) {
        this.mtn = mtn;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public int getSenderAge() {
        return senderAge;
    }

    public void setSenderAge(int senderAge) {
        this.senderAge = senderAge;
    }

    public String getBeneficiaryFullName() {
        return beneficiaryFullName;
    }

    public void setBeneficiaryFullName(String beneficiaryFullName) {
        this.beneficiaryFullName = beneficiaryFullName;
    }

    public int getBeneficiaryAge() {
        return beneficiaryAge;
    }

    public void setBeneficiaryAge(int beneficiaryAge) {
        this.beneficiaryAge = beneficiaryAge;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    // Getters and setters...

    public static class Issue {
        private Long issueId;

        private Boolean issueSolved;

        private String issueMessage;

        // Getters and setters...

        public Long getIssueId() {
            return issueId;
        }

        public void setIssueId(Long issueId) {
            this.issueId = issueId;
        }

        public Boolean getIssueSolved() {
            return issueSolved;
        }

        public void setIssueSolved(Boolean issueSolved) {
            this.issueSolved = issueSolved;
        }

        public String getIssueMessage() {
            return issueMessage;
        }

        public void setIssueMessage(String issueMessage) {
            this.issueMessage = issueMessage;
        }
    }
}
