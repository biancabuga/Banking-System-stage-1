package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public final class Account {
    private String iban;
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

    public void setFakeTransactions(final List<FakeTransaction> fakeTransactions) {
        this.fakeTransactions = fakeTransactions;
    }

    public Account(final String iban, final double balance,
                   final String currency, final String type,
                   final List<Card> cards) {
        this.iban = iban;
        this.balance = balance;
        this.currency = currency;
        this.type = type;
        this.cards = cards;
    }

    public String getIBAN() {
        return iban;
    }

    public void setIBAN(final String ibanul) {
        this.iban = ibanul;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(final double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }

    /**
     * metoda pentru afisare
     * @return
     */
    public ObjectNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode accountNode = mapper.createObjectNode();
        accountNode.put("IBAN", iban);
        accountNode.put("balance", balance);
        accountNode.put("currency", currency);
        accountNode.put("type", type);

        ArrayNode cardsNode = mapper.createArrayNode();
        for (Card card : cards) {
            cardsNode.add(card.toJson());
        }
        accountNode.set("cards", cardsNode);
        return accountNode;
    }

    /**
     * metoda pentru a adauga un card la un cont
     * @param card
     */
    public void addCard(final Card card) {
        if (cards == null) {
            cards = new ArrayList<>();
        }
        cards.add(card);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(final List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * metoda pentru a adauga o tranzactie la tranzactiile contului
     * @param transaction
     */
    public void addTransaction(final Transaction transaction) {
        if (transaction == null) {
            transactions.add(transaction);
        }
        transactions.add(transaction);
    }

    /**
     * metoda pentru a adauga o tranzactie falsa
     * (pe care nu dorim s-o printam
     * in PrintTransactions
     * dar este tot o tranzactie)
     * @param fakeTransaction
     */
    public void addFakeTransaction(final FakeTransaction fakeTransaction) {
        fakeTransactions.add(fakeTransaction);
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(final double interestRate) {
        this.interestRate = interestRate;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(final double minBalance) {
        this.minBalance = minBalance;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }
}
