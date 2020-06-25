package fr.esiha.katas.bank.account.driving.cucumber;

import fr.esiha.katas.bank.account.domain.AccountDepositService;
import io.cucumber.java8.En;
import org.joda.money.Money;

public class AccountDepositSteps implements En {
    private final AccountDepositService accountDepositService;
    private final AccountSteps accountSteps;

    public AccountDepositSteps(final AccountDepositService accountDepositService, final AccountSteps accountSteps) {
        this.accountDepositService = accountDepositService;
        this.accountSteps = accountSteps;
        declareSteps();
    }

    private void declareSteps() {
        When("{accountId} deposits {money} on her account", accountDepositService::deposit);
        Given("{accountId} has deposited {money} on her account", accountDepositService::deposit);
        Given("a deposit of {money}", this::depositOnCurrentAccount);
    }

    private void depositOnCurrentAccount(final Money depositAmount) {
        accountDepositService.deposit(accountSteps.getCurrentAccountId(), depositAmount);
    }
}
