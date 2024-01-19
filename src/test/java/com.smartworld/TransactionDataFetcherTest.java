package com.smartworld;

import com.smallworld.TransactionDataFetcher;
import com.smallworld.model.Transaction;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionDataFetcherTest {
    @Test
    void testGetTotalTransactionAmount() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        double totalAmount = dataFetcher.getTotalTransactionAmount();
        assertEquals(2889.17, totalAmount, 0.01);
    }

    @Test
    void testGetTotalTransactionAmountSentBy() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        double totalAmount = dataFetcher.getTotalTransactionAmountSentBy("Tom Shelby");
        assertEquals(678.06, totalAmount, 0.01);
    }

    @Test
    void testGetMaxTransactionAmount() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        double maxAmount = dataFetcher.getMaxTransactionAmount();
        assertEquals(985.0, maxAmount, 0.01);
    }

    @Test
    void testCountUniqueClients() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        long uniqueClients = dataFetcher.countUniqueClients();
        assertEquals(14, uniqueClients);
    }

    @Test
    void testHasOpenComplianceIssues() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        assertTrue(dataFetcher.hasOpenComplianceIssues("Tom Shelby"));
    }

    @Test
    void testGetTransactionsByBeneficiaryName() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        Map<String, List<Transaction>> transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();
        assertEquals(10, transactionsByBeneficiary.size());
    }

    @Test
    void testGetUnsolvedIssueIds() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        Set<Long> unsolvedIssueIds = dataFetcher.getUnsolvedIssueIds();
        assertEquals(Set.of(1L, 3L, 15L, 54L, 99L), unsolvedIssueIds);
    }

    @Test
    void testGetAllSolvedIssueMessages() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        List<String> solvedIssueMessages = dataFetcher.getAllSolvedIssueMessages();
        assertEquals(List.of("Never gonna give you up", "Never gonna let you down",
                "Never gonna run around and desert you"), solvedIssueMessages);
    }

    @Test
    void testGetTop3TransactionsByAmount() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        List<Transaction> top3Transactions = dataFetcher.getTop3TransactionsByAmount();
        assertEquals(3, top3Transactions.size());
        assertEquals(985.0, top3Transactions.get(0).getAmount(), 0.01);
        assertEquals(666.0, top3Transactions.get(1).getAmount(), 0.01);
        assertEquals(430.2, top3Transactions.get(2).getAmount(), 0.01);
    }

    @Test
    void testGetTopSender() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher();
        Optional<String> topSender = dataFetcher.getTopSender();
        assertTrue(topSender.isPresent());
        assertEquals("Arthur Shelby", topSender.get());
    }
}