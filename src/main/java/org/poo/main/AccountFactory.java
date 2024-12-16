package org.poo.main;

import java.util.List;

public final class AccountFactory {

    private AccountFactory() {
        // Constructor privat pentru a preveni instanțierea
    }

    /**
     *
     * @param iban
     * @param balance
     * @param currency
     * @param type
     * @param cards
     * @return
     */
    public static Account createAccount(final String iban,
                                        final double balance, final String currency,
                                        final String type, final List<Card> cards) {
        /**
         * cream conturile in functie de ce tip sunt
         */
        switch (type.toLowerCase()) {
            case "savings":
                return new Account(iban, balance, currency, "savings", cards);
            case "classic":
                return new Account(iban, balance, currency, "classic", cards);
            default:
                throw new IllegalArgumentException("Unsupported account type: " + type);
        }
    }
}
