package fr.esiha.katas.bank.account.driving.cucumber;

import fr.esiha.katas.bank.account.domain.AccountDepositService;
import io.cucumber.java8.En;

public class AccountDepositSteps implements En {
    public AccountDepositSteps(final AccountDepositService accountDepositService) {
        When("{accountId} deposits {money} on her account", accountDepositService::deposit);
    }
}
