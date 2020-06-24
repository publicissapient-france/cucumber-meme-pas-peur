package fr.esiha.katas.bank.account.driving.cucumber;

import fr.esiha.katas.bank.account.domain.AccountRepository;
import fr.esiha.katas.bank.account.domain.account.Account;
import io.cucumber.java8.En;
import org.joda.money.Money;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountRepositorySteps implements En {
    private final AccountRepository accountRepository;

    public AccountRepositorySteps(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        Then("{accountId}'s account has a balance of {money}", this::assertAccountHasBalance);
    }

    private void assertAccountHasBalance(final Account.Id accountId, final Money balance) {
        assertThat(accountRepository.get(accountId))
            .map(Account::getBalance)
            .contains(balance);
    }
}
