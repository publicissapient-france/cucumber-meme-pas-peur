package fr.esiha.katas.bank.account.driving.cucumber;

import fr.esiha.katas.bank.account.domain.account.Account;
import io.cucumber.java8.En;

public class AccountSteps implements En {
    public AccountSteps() {
        ParameterType("accountId", "[A-Z]\\w+", Account.Id::of);
    }
}
