package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class User implements JsonOutput {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;
    private int timestamp;
    private int k = 0;

    // Constructor privat pentru a preveni instanțierea directă
    private User(final UserBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.accounts = builder.accounts;
        this.timestamp = builder.timestamp;
        this.k = builder.k;
    }

    // Getteri și Setteri
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public ArrayList<Account> getAccounts() {
        return accounts;
    }
    public int getTimestamp() {
        return timestamp;
    }
    public int getK() {
        return k;
    }

    public void setTimestamp(final int timestamp) {
        this.timestamp = timestamp;
    }

    public void setK(final int k) {
        this.k = k;
    }

    @Override
    public ObjectNode toJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode userNode = mapper.createObjectNode();
        userNode.put("firstName", firstName);
        userNode.put("lastName", lastName);
        userNode.put("email", email);

        ArrayNode accountsNode = mapper.createArrayNode();
        for (Account account : accounts) {
            accountsNode.add(account.toJson());
        }
        userNode.set("accounts", accountsNode);
        return userNode;
    }

    /**
     * adaugarea unui cont la utilizator
     * @param account
     */
    public void addAccount(final Account account) {
        if (accounts == null) {
            accounts = new ArrayList<>();
        }
        accounts.add(account);
    }

    // Clasa Builder internă statică
    public static class UserBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private ArrayList<Account> accounts = new ArrayList<>();
        private int timestamp;
        private int k;

        /**
         * metoda pentru setarea fiecarui camp
         * @param theFirstName
         * @return
         */
        public final UserBuilder firstName(final String theFirstName) {
            this.firstName = theFirstName;
            return this;
        }

        /**
         * metoda pentru setarea fiecarui camp
         * @param theLastName
         * @return
         */
        public final UserBuilder lastName(final String theLastName) {
            this.lastName = theLastName;
            return this;
        }

        /**
         * metoda pentru setarea fiecarui camp
         * @param theEmail
         * @return
         */
        public final UserBuilder email(final String theEmail) {
            this.email = theEmail;
            return this;
        }

        /**
         * metoda pentru setarea fiecarui camp
         * @param theAccounts
         * @return
         */
        public final UserBuilder accounts(final ArrayList<Account> theAccounts) {
            this.accounts = theAccounts;
            return this;
        }


        /**
         * metoda pentru setarea fiecarui camp
         * @param theTimestamp
         * @return
         */
        public final UserBuilder timestamp(final int theTimestamp) {
            this.timestamp = theTimestamp;
            return this;
        }

        /**
         * metoda pentru setarea fiecarui camp
         * @param theK
         * @return
         */
        public final UserBuilder k(final int theK) {
            this.k = theK;
            return this;
        }

        /**
         * metoda pentru a construi obicetul User
         * @return
         */
        public final User build() {
            return new User(this);
        }
    }
}
