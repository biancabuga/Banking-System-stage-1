package org.poo.main;

public final class FakeTransaction extends Transaction {

    public FakeTransaction(final String description, final int timestamp) {
        // Call the parent constructor to initialize fields
        super(timestamp, "New account created");
    }
}
