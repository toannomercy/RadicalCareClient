package org.example.radicalmotor.Utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    public static String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }
}
