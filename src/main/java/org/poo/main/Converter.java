package org.poo.main;

import org.poo.fileio.ExchangeInput;
import java.util.List;

public class Converter {
    private static Converter instance;

    private Converter() {} // Singleton Pattern

    public static Converter getInstance() {
        if (instance == null) {
            instance = new Converter();
        }
        return instance;
    }

    public double convertCurrency(double amount, String from, String to, List<ExchangeInput> exchanges) {
        return recursiveConversion(amount, from, to, exchanges, 0);
    }

    private double recursiveConversion(double amount, String current, String target, List<ExchangeInput> exchanges, int depth) {
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
                double result = recursiveConversion(convertedAmount, exchange.getTo(), target, exchanges, depth + 1);
                if (result != -1) return result; // O cale validă a fost găsită
            } else if (exchange.getTo().equals(current)) {
                double convertedAmount = amount / exchange.getRate();
                double result = recursiveConversion(convertedAmount, exchange.getFrom(), target, exchanges, depth + 1);
                if (result != -1) return result; // O cale validă a fost găsită
            }
        }

        // Dacă nu există o cale validă, returnăm -1
        return -1;
    }
}

