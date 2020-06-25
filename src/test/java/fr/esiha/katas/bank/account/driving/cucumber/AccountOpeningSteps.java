package fr.esiha.katas.bank.account.driving.cucumber;

import fr.esiha.katas.bank.account.domain.AccountDepositService;
import fr.esiha.katas.bank.account.domain.AccountOpeningService;
import fr.esiha.katas.bank.account.domain.account.Account;
import io.cucumber.java8.En;
import org.joda.money.Money;

public class AccountOpeningSteps implements En {
    private final AccountOpeningService accountOpeningService;
    private final AccountDepositService accountDepositService;

    public AccountOpeningSteps(final AccountOpeningService accountOpeningService, final AccountDepositService accountDepositService) {
        this.accountOpeningService = accountOpeningService;
        this.accountDepositService = accountDepositService;
        declareSteps();
    }

    private void declareSteps() {
        Given("{accountId} has a newly opened bank account held in {currency}", accountOpeningService::openAccount);
        Given("{accountId} has a bank account with a balance of {money}", this::openAndDeposit);
    }

    private void openAndDeposit(final Account.Id accountId, final Money initialDeposit) {
        accountOpeningService.openAccount(accountId, initialDeposit.getCurrencyUnit());
        accountDepositService.deposit(accountId, initialDeposit);
    }
}
