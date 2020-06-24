package fr.esiha.katas.bank.account.driving.cucumber;

import fr.esiha.katas.bank.account.domain.AccountOpeningService;
import fr.esiha.katas.bank.account.domain.account.Account;
import io.cucumber.java8.En;
import org.joda.money.CurrencyUnit;

public class AccountOpeningSteps implements En {
    public AccountOpeningSteps(final AccountOpeningService accountOpeningService) {
        Given("{accountId} has a newly opened bank account held in {currency}", accountOpeningService::openAccount);
    }
}
