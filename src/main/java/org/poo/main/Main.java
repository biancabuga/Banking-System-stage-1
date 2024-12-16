package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.checker.Checker;
import org.poo.checker.CheckerConstants;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;
import org.poo.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        var sortedFiles = Arrays.stream(Objects.requireNonNull(directory.listFiles())).
                sorted(Comparator.comparingInt(Main::fileConsumer))
                .toList();

        for (File file : sortedFiles) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(CheckerConstants.TESTS_PATH + filePath1);
        ObjectInput inputData = objectMapper.readValue(file, ObjectInput.class);

        ArrayNode output = objectMapper.createArrayNode();

        /*
         * TODO Implement your function here
         *
         * How to add output to the output array?
         * There are multiple ways to do this, here is one example:
         *
         * ObjectMapper mapper = new ObjectMapper();
         *
         * ObjectNode objectNode = mapper.createObjectNode();
         * objectNode.put("field_name", "field_value");
         *
         * ArrayNode arrayNode = mapper.createArrayNode();
         * arrayNode.add(objectNode);
         *
         * output.add(arrayNode);
         * output.add(objectNode);
         *
         */
        Utils.resetRandom();
        List<User> users = new ArrayList<>();
        for (UserInput userInput : inputData.getUsers()) {
            users.add(new User.UserBuilder()
                    .firstName(userInput.getFirstName())
                    .lastName(userInput.getLastName())
                    .email(userInput.getEmail())
                    .accounts(new ArrayList<>())
                    .build()
            );
        }

        // Crearea cursurilor valutare
        Map<String, Double> exchangeRates = new HashMap<>();
        if (inputData.getExchangeRates() != null) {
            for (ExchangeInput exchange : inputData.getExchangeRates()) {
                exchangeRates.put(exchange.getFrom() + "_" + exchange.getTo(), exchange.getRate());
            }
        }

        // Procesarea comenzilor
        for (CommandInput command : inputData.getCommands()) {
            switch (command.getCommand()) {
                case "printUsers" -> {
                    ObjectNode commandOutput = objectMapper.createObjectNode();
                    commandOutput.put("command", "printUsers");
                    ArrayNode usersOutput = objectMapper.createArrayNode();
                    for (User user : users) {
                        for(Account account : user.getAccounts()) {
                            for(Card card : account.getCards()) {
                                if(card.getIsBlocked() == 1) {
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
                case "printTransactions" -> {
                    ObjectNode commandOutput = objectMapper.createObjectNode();
                    commandOutput.put("command", "printTransactions");
                    ArrayNode transactionsOutput = objectMapper.createArrayNode();
                    for (User user : users) {
                        if (user.getEmail().equals(command.getEmail())) {
                            ObjectNode infNode = objectMapper.createObjectNode();
                            infNode.put("description", "New account created");
                            infNode.put("timestamp", user.getTimestamp());
                            transactionsOutput.add(infNode);
                            for (Account account : user.getAccounts()) {
                                for (Transaction transaction : account.getTransactions()) {
                                    // Creăm manual nodul JSON pentru tranzacție
                                    ObjectNode transactionNode = objectMapper.createObjectNode();
                                    transactionNode.put("timestamp", transaction.getTimestamp());
                                    transactionNode.put("description", transaction.getDescription());
                                    if (transaction.getTransferType().equals("card_deletion")) {
                                        transactionNode.put("account", transaction.getAccount());
                                        transactionNode.put("card", transaction.getCardNumber());
                                        transactionNode.put("cardHolder", transaction.getCardHolder());
                                    } else if(transaction.getTransferType().equals("card_creation")) {
                                        transactionNode.put("account", transaction.getAccount());
                                        transactionNode.put("card", transaction.getCardNumber());
                                        transactionNode.put("cardHolder", transaction.getCardHolder());
                                    } else if(transaction.getTransferType().equals("online_payment")) {
                                        transactionNode.put("commerciant", transaction.getCommerciant());
                                        transactionNode.put("amount", transaction.getAmountToPay());
                                    } else if(transaction.getTransferType().equals("one_time_card_creation")) {
                                        transactionNode.put("account", transaction.getAccount());
                                        transactionNode.put("card", transaction.getCardNumber());
                                        transactionNode.put("cardHolder", transaction.getCardHolder());
                                    } else if(transaction.getTransferType().equals("split_payment")) {
                                        transactionNode.put("amount", transaction.getAmountToPay());
                                        transactionNode.put("currency", transaction.getCurrency());
                                        ArrayNode involvedAccounts = objectMapper.createArrayNode();
                                        for (String iban : transaction.getAccountsToSplit()) {
                                            involvedAccounts.add(iban);
                                        }
                                        transactionNode.set("involvedAccounts", involvedAccounts);
                                    } else if(transaction.getTransferType().equals("changeInterestRate")) {
                                        break;
                                    }
                                    else if(transaction.getTransferType().equals("sent")){
                                        if (transaction.getSenderIBAN() != null) {
                                            transactionNode.put("senderIBAN", transaction.getSenderIBAN());
                                        }
                                        if (transaction.getReceiverIBAN() != null) {
                                            transactionNode.put("receiverIBAN", transaction.getReceiverIBAN());
                                        }
                                        if (transaction.getAmount() != null) {
                                            transactionNode.put("amount", transaction.getAmount());
                                        }
                                        if (transaction.getTransferType() != null) {
                                            transactionNode.put("transferType", transaction.getTransferType());
                                        }

                                        // Adăugăm tranzacția la output

                                    }
                                    transactionsOutput.add(transactionNode);
                                }
                            }
                        }
                    }

                    // Adăugăm output-ul tranzacțiilor în răspunsul final
                    commandOutput.set("output", transactionsOutput);
                    commandOutput.put("timestamp", command.getTimestamp());
                    output.add(commandOutput);
                }

                case "addAccount" -> {
                    for (User user : users) {
                        if (user.getEmail().equals(command.getEmail())) {
                            // Generăm și validăm IBAN-ul
                            String generatedIBAN;
                            do {
                                generatedIBAN = Utils.generateIBAN();
                            } while (!(generatedIBAN.startsWith("RO") && generatedIBAN.length() == 24));
                            // Creăm contul
                            Account newAccount = AccountFactory.createAccount(
                                    generatedIBAN,
                                    0.0, // Sold inițial
                                    command.getCurrency(),
                                    command.getAccountType(),
                                    new ArrayList<>()
                            );


                            // Adăugăm contul în lista utilizatorului
                            FakeTransaction fakeTransaction = new FakeTransaction("New account created", command.getTimestamp());
                            newAccount.addFakeTransaction(fakeTransaction);
                            user.addAccount(newAccount);
                            user.setTimestamp(command.getTimestamp());
                        }
                    }
                }

                case "addFunds" -> {
                    for (User user : users) {
                        for (Account account : user.getAccounts()) {
                            // Verificăm dacă IBAN-ul corespunde contului
                            if (account.getIBAN().equals(command.getAccount())) {
                                // Adăugăm suma specificată în soldul contului
                                account.setBalance(account.getBalance() + command.getAmount());

                                // Adăugăm această tranzacție în lista de tranzacții a contului (dacă există tranzacții)

                            }
                        }
                    }
                }
                case "createCard" -> {
                    for (User user : users) {
                        String auxEmail = user.getEmail();
                        if (user.getEmail().equals(command.getEmail())) {
                            for (Account account : user.getAccounts()) {
                                if (account.getIBAN().equals(command.getAccount())) {
                                    // Generăm numărul cardului utilizând clasa Utils
                                    String generatedCardNumber = Utils.generateCardNumber();

                                    // Creăm cardul
                                    Card newCard = new Card(
                                            generatedCardNumber,
                                            "active", "classic" // Statusul cardului
                                    );

                                    // Adăugăm cardul în lista de carduri a contului
                                    account.addCard(newCard);

                                    // Înregistrăm tranzacția de creare a cardului
                                    Transaction transaction = new Transaction(
                                            command.getTimestamp(),
                                            "New card created",
                                            account.getIBAN(),
                                            generatedCardNumber,
                                            auxEmail,
                                            "card_creation", "1"
                                    );
                                    account.addTransaction(transaction);
                                }
                            }
                        }
                    }
                }
                case "createOneTimeCard" -> {
                    for (User user : users) {
                        if (user.getEmail().equals(command.getEmail())) {
                            String auxEmail = user.getEmail();
                            for (Account account : user.getAccounts()) {
                                if (account.getIBAN().equals(command.getAccount())) {
                                    // Generăm numărul unui card de tip "one time"
                                    String generatedCardNumber = Utils.generateCardNumber();

                                    // Creăm cardul de tip "one time"
                                    Card newCard = new Card(
                                            generatedCardNumber,
                                            "active", "one_time" // Statusul specific pentru acest tip de card
                                    );
                                    // Adăugăm cardul în lista de carduri a contului
                                    account.addCard(newCard);

                                    // Înregistrăm tranzacția de creare a cardului
                                    Transaction transaction = new Transaction(
                                            command.getTimestamp(),
                                            "New card created",
                                            account.getIBAN(), generatedCardNumber,
                                            auxEmail,
                                            "one_time_card_creation", "1"
                                    );
                                    account.addTransaction(transaction);
                                }
                            }
                        }
                    }
                }
                case "deleteAccount" -> {
                    boolean accountDeleted = false;
                    for (User user : users) {
                        if (user.getEmail().equals(command.getEmail())) {
                            // Iterăm prin conturile utilizatorului
                            List<Account> accounts = user.getAccounts();
                            for (int i = 0; i < accounts.size(); i++) {
                                Account account = accounts.get(i);
                                if (account.getIBAN().equals(command.getAccount())) {
                                    // Verificăm dacă balanța este 0.0
                                    if (account.getBalance() == 0.0) {
                                        // Ștergem toate cardurile asociate contului
                                        account.getCards().clear();

                                        // Ștergem contul din lista de conturi
                                        accounts.remove(i);
                                        accountDeleted = true;
                                    }
                                }
                            }
                        }
                    }

                    // Gestionăm output-ul pentru comanda `deleteAccount`
                    ObjectNode commandOutput = objectMapper.createObjectNode();
                    commandOutput.put("command", "deleteAccount");
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    if (accountDeleted) {
                        outputNode.put("success", "Account deleted");
                    } else {
                        outputNode.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
                    }
                    outputNode.put("timestamp", command.getTimestamp());
                    commandOutput.set("output", outputNode);
                    commandOutput.put("timestamp", command.getTimestamp());
                    output.add(commandOutput);
                }
                case "deleteCard" -> {
                    for (User user : users) {
                        String auxEmail = user.getEmail();
                        for (Account account : user.getAccounts()) {
                            String auxIBAN = account.getIBAN();
                            for (int i = 0; i < account.getCards().size(); i++) {
                                Card card = account.getCards().get(i);
                                if (card.getCardNUmber().equals(command.getCardNumber())) {
                                    // Șterge cardul din lista de carduri
                                    Card auxCard = account.getCards().get(i);
                                    account.getCards().remove(i);
                                    // Adaugă o tranzacție specifică pentru ștergerea cardului
                                    Transaction transaction = new Transaction(
                                            command.getTimestamp(),
                                            "The card has been destroyed",
                                            auxIBAN, // Nu este IBAN expeditor
                                            auxCard.getCardNUmber(),
                                            auxEmail,
                                            "card_deletion", "0"
                                    );
                                    account.addTransaction(transaction);

                                    break; // Ieșim din buclă după ce cardul este găsit și șters
                                }
                            }
                        }
                    }
                }
                case "setMinimumBalance" -> {
                    for (User user : users) {
                        // Verificăm dacă utilizatorul este cel corect
                        if (user.getEmail().equals(command.getEmail())) {
                            for (Account account : user.getAccounts()) {
                                // Verificăm IBAN-ul contului
                                if (account.getIBAN().equals(command.getAccount())) {
                                    // Setăm balanța minimă
                                    account.setMinBalance(command.getAmount());
                                    return; // Opriți execuția după ce setarea a fost realizată
                                }
                            }
                        }
                    }
                    // În cazul în care nu se găsește utilizatorul sau contul, nu facem nimic
                }
                case "checkCardStatus" -> {
                    boolean cardFound = false;

                    for (User user : users) {
                        for (Account account : user.getAccounts()) {
                            for (Card card : account.getCards()) {
                                if (card.getCardNUmber().equals(command.getCardNumber())) {
                                    cardFound = true;

                                    // Verificăm condițiile de balanță
                                    double balanceDifference = account.getBalance() - account.getMinBalance();

                                    if (account.getBalance() <= account.getMinBalance()) {
                                        // Blocăm cardul dacă balanța este sub minim
                                        card.setStatus("frozen");
                                        card.setIsBlocked(1);
                                        Transaction transaction = new Transaction(command.getTimestamp(), "You have reached the minimum amount of funds, the card will be frozen", "frozen card");
                                        account.addTransaction(transaction);
                                    } else if (balanceDifference <= 30) {
                                        // Schimbăm statusul în warning dacă diferența e <= 30
                                        card.setStatus("warning");
                                    } else if(account.getBalance() > account.getMinBalance()){
                                        // Cardul rămâne activ
                                        card.setStatus("active");
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    if (!cardFound) {
                        // Dacă nu s-a găsit cardul, adăugăm output-ul de eroare
                        ObjectNode commandOutput = objectMapper.createObjectNode();
                        commandOutput.put("command", "checkCardStatus");

                        ObjectNode outputNode = objectMapper.createObjectNode();
                        outputNode.put("description", "Card not found");
                        outputNode.put("timestamp", command.getTimestamp());

                        commandOutput.set("output", outputNode);
                        commandOutput.put("timestamp", command.getTimestamp());
                        output.add(commandOutput);
                    }
                }
                case "payOnline" -> {
                    boolean transactionSuccessful = false;
                    boolean cardFound = false;

                    for (User user : users) {
                        // Verificăm dacă utilizatorul corespunde
                        if (user.getEmail().equals(command.getEmail())) {
                            for (Account account : user.getAccounts()) {
                                for (Card card : account.getCards()) {
                                    // Verificăm dacă cardul corespunde și este activ
                                    if(card.getCardNUmber().equals(command.getCardNumber()) && card.getIsBlocked() == 1) {
                                        Transaction transaction = new Transaction(command.getTimestamp(), "The card is frozen", "frozen card");
                                        account.addTransaction(transaction);
                                        transactionSuccessful = false;
                                        cardFound = true;
                                        break;
                                    }else if (card.getCardNUmber().equals(command.getCardNumber()) && card.getStatus().equals("active")) {
                                        cardFound = true;
                                        double convertedAmount = command.getAmount();
                                        if (!account.getCurrency().equals(command.getCurrency())) {
                                            convertedAmount = Converter.getInstance().convertCurrency(
                                                    command.getAmount(),
                                                    command.getCurrency(),
                                                    account.getCurrency(),
                                                    Arrays.asList(inputData.getExchangeRates())
                                            );
                                        }
                                        if (account.getBalance() >= convertedAmount) {
                                            account.setBalance(account.getBalance() - convertedAmount);

                                            if (card.getTypeOfCard().equals("one_time")) {
                                                account.getCards().remove(card);
                                                // Generăm un nou card de tip OneTimeCard
                                                String newCardNumber = Utils.generateCardNumber();
                                                Card newCard = new Card(newCardNumber, "active", "one_time");
                                                account.addCard(newCard);

                                                // Înregistrăm tranzacția pentru noul card
                                                Transaction newCardTransaction = new Transaction(
                                                        command.getTimestamp(),
                                                        "New OneTimeCard created",
                                                        account.getIBAN(),
                                                        newCardNumber,
                                                        user.getEmail(),
                                                        "one_time_card_creation", "1"
                                                );
                                                account.addTransaction(newCardTransaction);
                                            } else {
                                                Transaction transaction = new Transaction(
                                                        command.getTimestamp(),
                                                        "Card payment",
                                                        command.getCommerciant(),
                                                        convertedAmount,
                                                        "online_payment"
                                                );
                                                account.addTransaction(transaction);
                                                transactionSuccessful = true;
                                            }
                                        } else {
                                            Transaction transaction = new Transaction(command.getTimestamp(), "Insufficient funds", "not enough money");
                                            account.addTransaction(transaction);
                                            transactionSuccessful = false;
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    if (!transactionSuccessful) {
                        ObjectNode commandOutput = objectMapper.createObjectNode();
                        commandOutput.put("command", "payOnline");
                        ObjectNode outputNode = objectMapper.createObjectNode();
                        if(!cardFound) {
                            outputNode.put("timestamp", command.getTimestamp());
                            commandOutput.set("output", outputNode);
                            commandOutput.put("timestamp", command.getTimestamp());
                            outputNode.put("description", "Card not found");
                            output.add(commandOutput);
                        }
                    }
                }
                case "sendMoney" -> {
                    boolean senderFound = false;
                    boolean receiverFound = false;
                    Account senderAccount = null;
                    Account receiverAccount = null;

                    // Găsim contul expeditor
                    for (User user : users) {
                        for (Account account : user.getAccounts()) {
                            if (account.getIBAN().equals(command.getAccount())) {
                                senderFound = true;
                                senderAccount = account;
                                break;
                            }
                        }
                        if (senderFound) break;
                    }

                    // Găsim contul destinatar
                    for (User user : users) {
                        for (Account account : user.getAccounts()) {
                            if (account.getIBAN().equals(command.getReceiver())) {
                                receiverFound = true;
                                receiverAccount = account;
                                break;
                            }
                        }
                        if (receiverFound) break;
                    }

                    // Verificăm dacă expeditorul și destinatarul există
                    if (!senderFound || !receiverFound) {
                        break;
                    }

                    // Verificăm dacă expeditorul are suficiente fonduri
                    if (senderAccount.getBalance() < command.getAmount()) {
                        Transaction transaction = new Transaction(command.getTimestamp(), "Insufficient funds", "not_enough");
                        senderAccount.addTransaction(transaction);
                        break;
                    }

                    // Calculăm suma convertită folosind metoda din clasa Converter
                    double convertedAmount = command.getAmount();
                    if (!senderAccount.getCurrency().equals(receiverAccount.getCurrency())) {
                        convertedAmount = Converter.getInstance().convertCurrency(
                                command.getAmount(),
                                senderAccount.getCurrency(),
                                receiverAccount.getCurrency(),
                                Arrays.asList(inputData.getExchangeRates()) // Conversia array-ului în listă
                        );
                    }

                    // Efectuăm transferul
                    senderAccount.setBalance(senderAccount.getBalance() - command.getAmount());
                    receiverAccount.setBalance(receiverAccount.getBalance() + convertedAmount);

                    // Înregistrăm tranzacția în contul expeditorului
                    Transaction senderTransaction = new Transaction(
                            command.getTimestamp(),
                            command.getDescription(),
                            senderAccount.getIBAN(),
                            receiverAccount.getIBAN(),
                            command.getAmount() + " " + senderAccount.getCurrency(),
                            "sent"
                    );
                    senderAccount.addTransaction(senderTransaction);

                    // Înregistrăm tranzacția în contul destinatarului
                    Transaction receiverTransaction = new Transaction(
                            command.getTimestamp(),
                            command.getDescription(),
                            senderAccount.getIBAN(),
                            receiverAccount.getIBAN(),
                            convertedAmount + " " + receiverAccount.getCurrency(),
                            "credit"
                    );
                    receiverAccount.addTransaction(receiverTransaction);
                }
                case "setAlias" -> {
                    boolean aliasSet = false;

                    for (User user : users) {
                        if (user.getEmail().equals(command.getEmail())) {
                            for (Account account : user.getAccounts()) {
                                // Verificăm dacă IBAN-ul este valid pentru utilizator
                                if (account.getIBAN().equals(command.getAccount())) {
                                    // Adăugăm alias-ul în cont
                                    account.setAlias(command.getAlias());
                                    aliasSet = true;
                                    break;
                                }
                            }
                        }
                    }
                } case "changeInterestRate" -> {
                    for(User user : users) {
                        for(Account account : user.getAccounts()) {
                            if(account.getIBAN().equals(command.getAccount())) {
                                account.setInterestRate(command.getInterestRate());
                                Transaction transaction = new Transaction(command.getTimestamp(), "", "changeInterestRate");
                                account.addTransaction(transaction);
                            }
                        }
                    }

                } case "splitPayment" -> {
                    boolean allAcountsValid = true;
                    boolean sufficientFunds = true;
                    double amountPerAccount = command.getAmount()/command.getAccounts().size();
                    double aux = amountPerAccount;
                    for(String iban : command.getAccounts()) {
                        boolean accountFound = false;
                        for(User user : users) {
                            for (Account account : user.getAccounts()) {
                                if(account.getIBAN().equals(iban)) {
                                    accountFound = true;
                                    break;
                                }
                            } if(accountFound) break;
                        } if(!accountFound) {
                            allAcountsValid = false;
                        }
                    }
                    if(allAcountsValid) {
                        ArrayNode involvedAccounts = objectMapper.createArrayNode();
                        for(String iban : command.getAccounts()) {
                            for(User user : users) {
                                for(Account account : user.getAccounts()) {
                                    if(account.getIBAN().equals(iban)) {
                                        double convertedAmount;
                                        if (!account.getCurrency().equals(command.getCurrency())) {
                                            convertedAmount = Converter.getInstance().convertCurrency(
                                                    amountPerAccount,
                                                    command.getCurrency(),
                                                    account.getCurrency(),
                                                    Arrays.asList(inputData.getExchangeRates())
                                            );
                                        } else {
                                            convertedAmount = aux;
                                        }
                                        account.setBalance(account.getBalance() - convertedAmount);
                                        involvedAccounts.add(iban);
                                        Transaction transaction = new Transaction(command.getTimestamp(),  String.format("Split payment of %.2f %s", command.getAmount(), command.getCurrency()), command.getAccounts(), command.getCurrency(), amountPerAccount, "split_payment");
                                        account.addTransaction(transaction);
                                    }
                                }
                            }
                        }

                    }
                } case "addInterest" -> {
                    for(User user : users) {
                        for(Account account : user.getAccounts()) {
                            if(account.getIBAN().equals(command.getAccount()) && account.getType().equals("savings")) {
                                account.setBalance(account.getBalance() + account.getInterestRate());
                                Transaction transaction = new Transaction(command.getTimestamp(), "", "addInterest");
                                account.addTransaction(transaction);
                            }
                        }
                    }
                } case "report" -> {
                    // Locate the account for the report
                    Account targetAccount = null;
                    for (User user : users) {
                        for (Account account : user.getAccounts()) {
                            if (account.getIBAN().equals(command.getAccount())) {
                                targetAccount = account;
                                break;
                            }
                        }
                        if (targetAccount != null) break;
                    }

                    ObjectNode commandOutput = objectMapper.createObjectNode();
                    commandOutput.put("command", "report");

                    if (targetAccount == null) {
                        // Account not found
                        ObjectNode errorOutput = objectMapper.createObjectNode();
                        errorOutput.put("description", "Account not found");
                        errorOutput.put("timestamp", command.getTimestamp());
                        commandOutput.set("output", errorOutput);
                    } else {
                        ObjectNode accountReport = objectMapper.createObjectNode();
                        accountReport.put("IBAN", targetAccount.getIBAN());
                        accountReport.put("balance", targetAccount.getBalance());
                        accountReport.put("currency", targetAccount.getCurrency());

                        ArrayNode transactionsOutput = objectMapper.createArrayNode();

                        for(FakeTransaction fakeTransaction : targetAccount.getFakeTransactions()){
                            if(fakeTransaction.getTimestamp() >= command.getStartTimestamp()
                                    && fakeTransaction.getTimestamp() <= command.getEndTimestamp()) {
                                ObjectNode fakeTransactionNode = objectMapper.createObjectNode();
                                fakeTransactionNode.put("timestamp", fakeTransaction.getTimestamp());
                                fakeTransactionNode.put("description", fakeTransaction.getDescription());
                                transactionsOutput.add(fakeTransactionNode);
                            }
                        }

                        for (Transaction transaction : targetAccount.getTransactions()) {
                            if (transaction.getTimestamp() >= command.getStartTimestamp()
                                    && transaction.getTimestamp() <= command.getEndTimestamp()) {
                                // Add only relevant transactions for savings accounts
                                if (targetAccount.getType().equals("savings") && (!transaction.getTransferType().equals("addInterest") || !(transaction.getTransferType().equals("changeInterestRate")))) {
                                    continue;
                                }

                                ObjectNode transactionNode = objectMapper.createObjectNode();
                                transactionNode.put("timestamp", transaction.getTimestamp());
                                transactionNode.put("description", transaction.getDescription());
                                if(transaction.getTransferType().equals("online_payment")) {
                                    transactionNode.put("commerciant", transaction.getCommerciant());
                                    transactionNode.put("amount", transaction.getAmountToPay());
                                } else if(transaction.getTransferType().equals("card_creation")) {
                                    transactionNode.put("account", transaction.getAccount());
                                    transactionNode.put("card", transaction.getCardNumber());
                                    transactionNode.put("cardHolder", transaction.getCardHolder());
                                } else if(transaction.getTransferType().equals("sent")) {
                                    transactionNode.put("amount", transaction.getAmount());
                                    transactionNode.put("description", transaction.getDescription());
                                    transactionNode.put("receiverIBAN", transaction.getReceiverIBAN());
                                    transactionNode.put("senderIBAN", transaction.getSenderIBAN());
                                    transactionNode.put("transferType", transaction.getTransferType());
                                }
                                transactionsOutput.add(transactionNode);
                            }
                        }

                        accountReport.set("transactions", transactionsOutput);
                        commandOutput.set("output", accountReport);
                    }

                    commandOutput.put("timestamp", command.getTimestamp());
                    output.add(commandOutput);
                } case "spendingsReport" -> {
                    ObjectNode commandOutput = objectMapper.createObjectNode();
                    commandOutput.put("command", "spendingsReport");

                    // Găsește contul țintă
                    Account targetAccount = null;
                    for (User user : users) {
                        for (Account account : user.getAccounts()) {
                            if (account.getIBAN().equals(command.getAccount())) {
                                targetAccount = account;
                                break;
                            }
                        }
                        if (targetAccount != null) break;
                    }

                    if (targetAccount == null) {
                        // Dacă contul nu este găsit
                        ObjectNode errorOutput = objectMapper.createObjectNode();
                        errorOutput.put("error", "Account not found");
                        errorOutput.put("timestamp", command.getTimestamp());
                        commandOutput.set("output", errorOutput);
                    } else {
                        ObjectNode spendingsReport = objectMapper.createObjectNode();
                        spendingsReport.put("IBAN", targetAccount.getIBAN());
                        spendingsReport.put("balance", targetAccount.getBalance());
                        spendingsReport.put("currency", targetAccount.getCurrency());

                        // Calculează sumele cheltuite pe comercianți
                        Map<String, Double> merchantSpendings = new HashMap<>();
                        ArrayNode transactionsOutput = objectMapper.createArrayNode();

                        for (Transaction transaction : targetAccount.getTransactions()) {
                            if (transaction.getTransferType().equals("online_payment") &&
                                    transaction.getTimestamp() >= command.getStartTimestamp() &&
                                    transaction.getTimestamp() <= command.getEndTimestamp()) {

                                // Adaugă la totalul comerciantului
                                merchantSpendings.put(transaction.getCommerciant(),
                                        merchantSpendings.getOrDefault(transaction.getCommerciant(), 0.0) + transaction.getAmountToPay());

                                // Adaugă tranzacția în secțiunea de tranzacții
                                ObjectNode transactionNode = objectMapper.createObjectNode();
                                transactionNode.put("timestamp", transaction.getTimestamp());
                                transactionNode.put("description", transaction.getDescription());
                                transactionNode.put("amount", transaction.getAmountToPay());
                                transactionNode.put("commerciant", transaction.getCommerciant());
                                transactionsOutput.add(transactionNode);
                            }
                        }

                        // Adaugă comercianții în output în ordine alfabetică
                        ArrayNode merchantsOutput = objectMapper.createArrayNode();
                        merchantSpendings.entrySet().stream()
                                .sorted(Map.Entry.comparingByKey())
                                .forEach(entry -> {
                                    ObjectNode merchantNode = objectMapper.createObjectNode();
                                    merchantNode.put("commerciant", entry.getKey());
                                    merchantNode.put("total", entry.getValue());
                                    merchantsOutput.add(merchantNode);
                                });

                        spendingsReport.set("commerciants", merchantsOutput);
                        spendingsReport.set("transactions", transactionsOutput);
                        commandOutput.set("output", spendingsReport);
                    }

                    commandOutput.put("timestamp", command.getTimestamp());
                    output.add(commandOutput);
                }

                default -> {
                    ObjectNode commandOutput = objectMapper.createObjectNode();
                    commandOutput.put("command", command.getCommand());
                    commandOutput.put("error", "Command not implemented");
                    commandOutput.put("timestamp", command.getTimestamp());
                    output.add(commandOutput);
                }
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

    /**
     * Method used for extracting the test number from the file name.
     *
     * @param file the input file
     * @return the extracted numbers
     */
    public static int fileConsumer(final File file) {
        String fileName = file.getName()
                .replaceAll(CheckerConstants.DIGIT_REGEX, CheckerConstants.EMPTY_STR);
        return Integer.parseInt(fileName.substring(0, 2));
    }
}
