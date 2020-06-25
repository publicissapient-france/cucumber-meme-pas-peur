package fr.esiha.katas.bank.account.driving.cucumber;

import fr.esiha.katas.bank.account.domain.AccountHistoryService;
import fr.esiha.katas.bank.account.domain.account.Account;
import fr.esiha.katas.bank.account.domain.account.History;
import io.cucumber.java8.En;

public class AccountHistorySteps implements En {
    private final AccountHistoryService accountHistoryService;
    private History lastHistory;

    public AccountHistorySteps(final AccountHistoryService accountHistoryService) {
        this.accountHistoryService = accountHistoryService;
        declareSteps();
    }

    private void declareSteps() {
        When("{accountId} checks the history of her account", this::getAccountHistory);
    }

    private void getAccountHistory(final Account.Id accountId) {
        this.lastHistory = accountHistoryService.getAccountHistory(accountId);
    }

    public History getLastHistory() {
        return lastHistory;
    }
}
