package org.poo.fileio;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class ObjectInput {
    private UserInput[] users;
    private ExchangeInput[] exchangeRates;
    private CommandInput[] commands;
    private CommerciantInput[] commerciants;

    public UserInput[] getUsers() {
        return users;
    }

    public void setUsers(UserInput[] users) {
        this.users = users;
    }

    public ExchangeInput[] getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(ExchangeInput[] exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public CommandInput[] getCommands() {
        return commands;
    }

    public void setCommands(CommandInput[] commands) {
        this.commands = commands;
    }

    public CommerciantInput[] getCommerciants() {
        return commerciants;
    }

    public void setCommerciants(CommerciantInput[] commerciants) {
        this.commerciants = commerciants;
    }
}
