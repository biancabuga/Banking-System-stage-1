package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public final class User implements JsonOutput{
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;
    private int timestamp;
    private int k = 0;

    // Constructor privat pentru a preveni instanțierea directă
    private User(UserBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.accounts = builder.accounts;
        this.timestamp = builder.timestamp;
        this.k = builder.k;
    }

    // Getteri și Setteri
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public ArrayList<Account> getAccounts() { return accounts; }
    public int getTimestamp() { return timestamp; }
    public int getK() { return k; }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setK(int k) {
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

    public void addAccount(Account account) {
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

        // Metodă fluentă pentru setarea fiecărui câmp
        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder accounts(ArrayList<Account> accounts) {
            this.accounts = accounts;
            return this;
        }

        public UserBuilder timestamp(int timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public UserBuilder k(int k) {
            this.k = k;
            return this;
        }

        // Metodă pentru a construi obiectul final User
        public User build() {
            return new User(this);
        }
    }
}
