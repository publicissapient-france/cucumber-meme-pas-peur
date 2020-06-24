package fr.esiha.katas.bank.account.driving.cucumber;

import io.cucumber.java8.En;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public class JodaMoneySteps implements En {
    private static final String CURRENCY_REGEX = "[A-Z]{3}";

    public JodaMoneySteps() {
        ParameterType("currency", CURRENCY_REGEX, CurrencyUnit::of);
        ParameterType("money", CURRENCY_REGEX + " \\S+", Money::parse);
    }
}
