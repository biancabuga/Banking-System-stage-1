package org.poo.main;

import java.util.List;

public class AccountFactory {

    private AccountFactory() {
        // Constructor privat pentru a preveni instanțierea
    }

    public static Account createAccount(String IBAN, double balance, String currency, String type, List<Card> cards) {
        switch (type.toLowerCase()) {
            case "savings":
                return new Account(IBAN, balance, currency, "savings", cards);
            case "classic":
                return new Account(IBAN, balance, currency, "classic", cards);
            default:
                throw new IllegalArgumentException("Unsupported account type: " + type);
        }
    }
}
