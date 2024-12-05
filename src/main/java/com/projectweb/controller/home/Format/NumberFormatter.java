package com.projectweb.controller.home.Format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NumberFormatter {
    public static String format(BigDecimal number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.'); // Set the grouping separator to a dot

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###", symbols);
        return decimalFormat.format(number);
    }
}


