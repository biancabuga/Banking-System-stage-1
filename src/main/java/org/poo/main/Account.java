package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public final class Account {
    private String IBAN;
    private double balance;
    private String currency;
    private String type;
    private List<Card> cards;
    private double interestRate;
    private List<Transaction> transactions = new ArrayList<>();
    private double minBalance;
    private String alias;
    private List<FakeTransaction> fakeTransactions = new ArrayList<>();

    public List<FakeTransaction> getFakeTransactions() {
        return fakeTransactions;
    }

    public void setFakeTransactions(List<FakeTransaction> fakeTransactions) {
        this.fakeTransactions = fakeTransactions;
    }

    public Account(String IBAN, double balance, String currency, String type, List<Card> cards) {
        this.IBAN = IBAN;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = cards;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
    public ObjectNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode accountNode = mapper.createObjectNode();
        accountNode.put("IBAN", IBAN);
        accountNode.put("balance", balance);
        accountNode.put("currency", currency);
        accountNode.put("type", type);

        ArrayNode cardsNode = mapper.createArrayNode();
        for(Card card : cards) {
            cardsNode.add(card.toJson());
        }
        accountNode.set("cards", cardsNode);
        return accountNode;
    }
    public void addCard(Card card) {
        if(cards == null) {
            cards = new ArrayList<>();
        }
        cards.add(card);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    public void addTransaction(Transaction transaction) {
        if(transaction == null) {
            transactions.add(transaction);
        }
        transactions.add(transaction);
    }

    public void addFakeTransaction(FakeTransaction fakeTransaction) {
        fakeTransactions.add(fakeTransaction);
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(double minBalance) {
        this.minBalance = minBalance;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
