package fr.esiha.katas.bank.account.driving.cucumber;

import fr.esiha.katas.bank.account.domain.account.Account;
import io.cucumber.java8.En;

public class AccountSteps implements En {
    private Account.Id currentAccountId;

    public AccountSteps() {
        ParameterType("accountId", "[A-Z]\\w+", Account.Id::of);
        Given("{accountId} has made operations on her account:", this::setCurrentAccountId);
    }

    private void setCurrentAccountId(final Account.Id accountId) {
        this.currentAccountId = accountId;
    }

    public Account.Id getCurrentAccountId() {
        return currentAccountId;
    }
}
