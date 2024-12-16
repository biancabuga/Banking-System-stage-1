package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;
import java.util.Map;

public class CommandContext {
    private final List<User> users;
    private final Map<String, Double> exchangeRates;
    private final ArrayNode output;
    private final ObjectMapper objectMapper;

    public CommandContext(List<User> users, Map<String, Double> exchangeRates, ArrayNode output, ObjectMapper objectMapper) {
        this.users = users;
        this.exchangeRates = exchangeRates;
        this.output = output;
        this.objectMapper = objectMapper;
    }

    public List<User> getUsers() {
        return users;
    }

    public Map<String, Double> getExchangeRates() {
        return exchangeRates;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}

