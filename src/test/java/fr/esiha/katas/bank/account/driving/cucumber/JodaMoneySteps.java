package fr.esiha.katas.bank.account.driving.cucumber;

import io.cucumber.java8.En;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public class JodaMoneySteps implements En {
    private static final String CURRENCY_REGEX = "[A-Z]{3}";
    public static final String MONEY_REGEX = CURRENCY_REGEX + " \\S+";

    public JodaMoneySteps() {
        ParameterType("currency", CURRENCY_REGEX, CurrencyUnit::of);
        ParameterType("money", MONEY_REGEX, Money::parse);
    }
}
