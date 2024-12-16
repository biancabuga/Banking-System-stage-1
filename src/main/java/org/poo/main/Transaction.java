package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class Transaction implements JsonOutput{
    private int timestamp;
    private String description;
    private String senderIBAN;
    private String receiverIBAN;
    private String amount;
    private String transferType;
    private String cardNumber;
    private String cardHolder;
    private String account;
    private double amountToPay;
    private String commerciant;
    private String currency;
    List<String> accountsToSplit;

    public List<String> getAccountsToSplit() {
        return accountsToSplit;
    }

    public void setAccountsToSplit(List<String> accountsToSplit) {
        this.accountsToSplit = accountsToSplit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public void setCommerciant(String commerciant) {
        this.commerciant = commerciant;
    }

    public double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSenderIBAN() {
        return senderIBAN;
    }

    public void setSenderIBAN(String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }

    public String getReceiverIBAN() {
        return receiverIBAN;
    }

    public void setReceiverIBAN(String receiverIBAN) {
        this.receiverIBAN = receiverIBAN;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public Transaction(String transferType){
        this.transferType = transferType;
    }

    public Transaction(int timestamp, String description, String senderIBAN, String receiverIBAN, String amount, String transferType) {
        this.timestamp = timestamp;
        this.description = description;
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = amount;
        this.transferType = transferType;
    }

    public Transaction(int timestamp, String description, String account, String cardNumber, String cardHolder, String transferType, String amount) {
        this.timestamp = timestamp;
        this.description = description;
        this.account = account;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.transferType = transferType;
    }

    public Transaction(int timestamp, String description, String commerciant, double amountToPay, String transferType) {
        this.timestamp = timestamp;
        this.description = description;
        this.commerciant = commerciant;
        this.amountToPay = amountToPay;
        this.transferType = transferType;
    }

    public Transaction(int timestamp, String description, String transferType) {
        this.timestamp = timestamp;
        this.description = description;
        this.transferType = transferType;
    }

    public Transaction(int timestamp, String description) {
        this.timestamp = timestamp;
        this.description = description;
    }

    public Transaction(int timestamp, String description, List<String> AccountsForSplit, String currency, double amountToPay, String transferType) {
        this.timestamp = timestamp;
        this.description = description;
        this.accountsToSplit = AccountsForSplit;
        this.currency = currency;
        this.amountToPay = amountToPay;
        this.transferType = transferType;
    }

    @Override
    public ObjectNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode transactionNode = mapper.createObjectNode();
        transactionNode.put("timestamp", timestamp);
        transactionNode.put("description", description);
        if (senderIBAN != null && !senderIBAN.isEmpty()) {
            transactionNode.put("senderIBAN", senderIBAN);
        }
        if (receiverIBAN != null && !receiverIBAN.isEmpty()) {
            transactionNode.put("receiverIBAN", receiverIBAN);
        }
        if (amount != null && !amount.isEmpty()) {
            transactionNode.put("amount", amount);
        }
        if (transferType != null && !transferType.isEmpty()) {
            transactionNode.put("transferType", transferType);
        }
        return transactionNode;
    }
}
