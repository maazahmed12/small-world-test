package com.smallworld;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.model.Transaction;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TransactionDataFetcher {


    private final List<Transaction> transactions;

    public TransactionDataFetcher() {
        this.transactions = mapTransactionsFromJson();
    }

    /**
     * Map transaction.json to Transaction model
     */
    public static List<Transaction> mapTransactionsFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> jsonTransactions = null;
        try {
            jsonTransactions = mapper.readValue(
                    new File("src/main/resources/transactions.json"), List.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Transaction> transactions = new ArrayList<>();
        Map<Long, Transaction> transactionMap = new HashMap<>(); // Map to group transactions by MTN

        for (Map<String, Object> jsonTransaction : jsonTransactions) {
            Long mtn = Long.valueOf(jsonTransaction.get("mtn").toString());
            Transaction transaction = transactionMap.get(mtn);

            if (Objects.isNull(transaction)) {
                transaction = new Transaction();
                transaction.setMtn(mtn);
                transaction.setAmount((Double) jsonTransaction.get("amount"));
                transaction.setSenderFullName((String) jsonTransaction.get("senderFullName"));
                transaction.setSenderAge((Integer) jsonTransaction.get("senderAge"));
                transaction.setBeneficiaryFullName((String) jsonTransaction.get("beneficiaryFullName"));
                transaction.setBeneficiaryAge((Integer) jsonTransaction.get("beneficiaryAge"));
                transaction.setIssues(new ArrayList<>());
                transactions.add(transaction);
                transactionMap.put(mtn, transaction);
            }

            Long issueId = Objects.nonNull(jsonTransaction.get("issueId")) ?
                    Long.valueOf(jsonTransaction.get("issueId").toString()) : null;

            if (Objects.nonNull(issueId)) {
                Transaction.Issue issue = new Transaction.Issue();
                issue.setIssueId(issueId);
                issue.setIssueSolved((Boolean) jsonTransaction.get("issueSolved"));
                issue.setIssueMessage((String) jsonTransaction.get("issueMessage"));
                transaction.getIssues().add(issue);
            }
        }
        return transactions;
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        return transactions.stream()
                .filter(transaction -> transaction.getSenderFullName().equals(senderFullName))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .max()
                .orElse(0.0);
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        return transactions.stream()
                .flatMap(transaction -> Stream.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName()))
                .distinct()
                .count();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        return transactions.stream()
                .anyMatch(transaction -> (clientFullName.equals(transaction.getSenderFullName()) ||
                        clientFullName.equals(transaction.getBeneficiaryFullName())) &&
                        hasUnsolvedIssue(transaction));
    }

    private boolean hasUnsolvedIssue(Transaction transaction) {
        return Objects.nonNull(transaction.getIssues()) &&
                transaction.getIssues().stream()
                        .anyMatch(issue -> !issue.getIssueSolved());
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Long> getUnsolvedIssueIds() {
        return transactions.stream()
                .flatMap(transaction -> transaction.getIssues().stream())
                .filter(issue -> Objects.nonNull(issue.getIssueId()) && !issue.getIssueSolved())
                .map(Transaction.Issue::getIssueId)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        return transactions.stream()
                .flatMap(transaction -> transaction.getIssues().stream())
                .filter(issue -> issue.getIssueMessage() != null && issue.getIssueSolved())
                .map(Transaction.Issue::getIssueMessage)
                .toList();
    }

    /**
     * Returns the 3 transactions with highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {

        return transactions.stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3)
                .toList();
    }

    /**
     * Returns the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getSenderFullName,
                        Collectors.summingDouble(Transaction::getAmount)))
                .entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey);
    }

}
