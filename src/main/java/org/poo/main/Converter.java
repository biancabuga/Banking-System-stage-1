package org.poo.main;

import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public final class Converter {
    private static Converter instance;

    private Converter() {

    } // Singleton Pattern

    public static Converter getInstance() {
        if (instance == null) {
            instance = new Converter();
        }
        return instance;
    }

    /**
     * instanta pentru Singleton Pattern
     */
    // Metoda principală pentru conversie
    public double convert(double amount, String from, String to,  List<ExchangeInput> exchanges) {
        // Construim graful
        Map<String, List<ExchangeInput>> graph = buildGraph(exchanges);

        // Folosim algoritmul DFS pentru a găsi rata de schimb
        Set<String> visited = new HashSet<>();
        double result = dfsConvert(graph, from, to, amount, visited);

        if (result == -1) {
            throw new IllegalArgumentException("Conversion not possible");
        }
        return result;
    }

    // Construirea grafului din lista de ExchangeInput-uri
    private Map<String, List<ExchangeInput>> buildGraph(List<ExchangeInput> exchanges) {
        Map<String, List<ExchangeInput>> graph = new HashMap<>();

        for (ExchangeInput exchange : exchanges) {
            graph.putIfAbsent(exchange.getFrom(), new ArrayList<>());
            graph.putIfAbsent(exchange.getTo(), new ArrayList<>());

            // Adăugăm rata directă
            graph.get(exchange.getFrom()).add(exchange);

            // Adăugăm rata inversă
            ExchangeInput inverseExchange = new ExchangeInput();
            inverseExchange.setFrom(exchange.getTo());
            inverseExchange.setTo(exchange.getFrom());
            inverseExchange.setRate(1.0 / exchange.getRate());
            graph.get(exchange.getTo()).add(inverseExchange);
        }

        return graph;
    }

    // Algoritmul DFS pentru a calcula conversia
    private double dfsConvert(Map<String, List<ExchangeInput>> graph, String current, String target, double amount, Set<String> visited) {
        if (current.equals(target)) {
            return amount;
        }

        visited.add(current);
        double result = -1;

        for (ExchangeInput neighbor : graph.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(neighbor.getTo())) {
                double convertedAmount = dfsConvert(graph, neighbor.getTo(), target, amount * neighbor.getRate(), visited);
                if (convertedAmount != -1) {
                    result = convertedAmount;
                    break; // Ieșim imediat ce găsim o soluție validă
                }
            }
        }

        visited.remove(current);
        return result;
    }

    /**
     *
     * @param amount
     * @param from
     * @param to
     * @param exchanges
     * @return
     */
    public double convertCurrency(final double amount, final String from,
                                  final String to, final List<ExchangeInput> exchanges) {
        return recursiveConversion(amount, from, to, exchanges, 0);
    }

    private double recursiveConversion(final double amount, final String current,
                                       final String target, final List<ExchangeInput> exchanges,
                                       final int depth) {
        // Dacă am ajuns la valuta țintă, returnăm suma curentă
        if (current.equals(target)) {
            return amount;
        }

        // Limităm adâncimea recursivă pentru a preveni bucle infinite
        if (depth > exchanges.size()) {
            return -1; // Conversie imposibilă
        }

        // Explorăm toate opțiunile disponibile
        for (ExchangeInput exchange : exchanges) {
            if (exchange.getFrom().equals(current)) {
                double convertedAmount = amount * exchange.getRate();
                double result = recursiveConversion(convertedAmount,
                        exchange.getTo(), target, exchanges, depth + 1);
                if (result != -1) {
                    return result; // O cale validă a fost găsită
                }
            } else if (exchange.getTo().equals(current)) {
                double convertedAmount = amount / exchange.getRate();
                double result = recursiveConversion(convertedAmount,
                        exchange.getFrom(), target, exchanges, depth + 1);
                if (result != -1) {
                    return result; // O cale validă a fost găsită
                }
            }
        }

        // Dacă nu există o cale validă, returnăm -1
        return -1;
    }
}



