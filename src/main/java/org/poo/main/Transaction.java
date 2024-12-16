package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class Transaction implements JsonOutput {
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
    private List<String> accountsToSplit;

    public final List<String> getAccountsToSplit() {
        /**
         * getter pentru conturile care impart plata
         */
        return accountsToSplit;
    }

    public final void setAccountsToSplit(final List<String> accountsToSplit) {
        /**
         * setter pentru conturile care impart plata
         */
        this.accountsToSplit = accountsToSplit;
    }

    public final String getCurrency() {
        /**
         * getter pentru moneda curenta a contului
         */
        return currency;
    }

    public final void setCurrency(final String currency) {
        /**
         * setter pentru moneda curenta a contului
         */
        this.currency = currency;
    }

    public final String getCommerciant() {
        /**
         * getter pentru comerciantul catre care facem plata
         */
        return commerciant;
    }

    public final void setCommerciant(final String commerciant) {
        /**
         * setter pentru comerciantul catre care facem plata
         */
        this.commerciant = commerciant;
    }

    public final double getAmountToPay() {
        /**
         * getter pentru suma de plata
         */
        return amountToPay;
    }

    public final void setAmountToPay(final double amountToPay) {
        /**
         * setter pentru suma de plata
         */
        this.amountToPay = amountToPay;
    }

    public final String getAccount() {
        /**
         * getter pentru contul care face plata
         */
        return account;
    }

    public final void setAccount(final String account) {
        /**
         * setter pentru contul care face plata
         */
        this.account = account;
    }

    public final String getCardNumber() {
        /**
         * getter pentru numarul de card
         */
        return cardNumber;
    }

    public final void setCardNumber(final String cardNumber) {
        /**
         * setter pentru numarul de card
         */
        this.cardNumber = cardNumber;
    }

    public final String getCardHolder() {
        return cardHolder;
    }

    public final void setCardHolder(final String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public final int getTimestamp() {
        return timestamp;
    }

    public final void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final String getSenderIBAN() {
        return senderIBAN;
    }

    public final void setSenderIBAN(final String senderIBAN) {
        this.senderIBAN = senderIBAN;
    }

    public final String getReceiverIBAN() {
        return receiverIBAN;
    }

    public final void setReceiverIBAN(final String receiverIBAN) {
        this.receiverIBAN = receiverIBAN;
    }

    public final String getAmount() {
        return amount;
    }

    public final void setAmount(final String amount) {
        this.amount = amount;
    }

    public final String getTransferType() {
        return transferType;
    }

    public final void setTransferType(final String transferType) {
        this.transferType = transferType;
    }

    public Transaction(final String transferType) {
        this.transferType = transferType;
    }

    public Transaction(final int timestamp, final String description, final String senderIBAN,
                       final String receiverIBAN, final String amount, final String transferType) {
        this.timestamp = timestamp;
        this.description = description;
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = amount;
        this.transferType = transferType;
    }

    public Transaction(final int timestamp, final String description, final String account,
                       final String cardNumber, final String cardHolder,
                       final String transferType, final String amount) {
        this.timestamp = timestamp;
        this.description = description;
        this.account = account;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.transferType = transferType;
    }

    public Transaction(final int timestamp, final String description, final String commerciant,
                       final double amountToPay, final String transferType) {
        this.timestamp = timestamp;
        this.description = description;
        this.commerciant = commerciant;
        this.amountToPay = amountToPay;
        this.transferType = transferType;
    }

    public Transaction(final int timestamp, final String description, final String transferType) {
        this.timestamp = timestamp;
        this.description = description;
        this.transferType = transferType;
    }

    public Transaction(final int timestamp, final String description) {
        this.timestamp = timestamp;
        this.description = description;
    }

    public Transaction(final int timestamp, final String description,
                       final List<String> accountsForSplit,
                       final String currency, final double amountToPay,
                       final String transferType) {
        this.timestamp = timestamp;
        this.description = description;
        this.accountsToSplit = accountsForSplit;
        this.currency = currency;
        this.amountToPay = amountToPay;
        this.transferType = transferType;
    }

    @Override
    public final ObjectNode toJson() {
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
