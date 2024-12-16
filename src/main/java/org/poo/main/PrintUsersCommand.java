package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import java.util.List;

public class PrintUsersCommand implements Command {
    private final CommandContext context;
    private final CommandInput command;

    public PrintUsersCommand(CommandContext context, CommandInput command) {
        this.context = context;
        this.command = command;
    }

    @Override
    public void execute() {
        ObjectMapper objectMapper = context.getObjectMapper();
        List<User> users = context.getUsers();
        ArrayNode output = context.getOutput();

        ObjectNode commandOutput = objectMapper.createObjectNode();
        commandOutput.put("command", "printUsers");

        ArrayNode usersOutput = objectMapper.createArrayNode();
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getIsBlocked() == 1) {
                        card.setStatus("frozen");
                    } else {
                        card.setStatus("active");
                    }
                }
            }
            usersOutput.add(user.toJson());
        }

        commandOutput.set("output", usersOutput);
        commandOutput.put("timestamp", command.getTimestamp());
        output.add(commandOutput);
    }
}

