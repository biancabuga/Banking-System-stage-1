package org.poo.main;

import java.util.List;

public final class FakeTransaction extends Transaction {

    public FakeTransaction(String description, int timestamp) {
        // Call the parent constructor to initialize fields
        super(timestamp, "New account created");
    }
}
