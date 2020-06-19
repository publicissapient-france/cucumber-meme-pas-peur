package fr.esiha.katas.bank.account.domain;

import fr.esiha.katas.bank.account.domain.account.Account;
import org.joda.money.CurrencyUnit;

import static java.util.Objects.requireNonNull;

public final class AccountService implements AccountOpeningService {
    private final AccountRepository repository;

    public AccountService(final AccountRepository repository) {
        this.repository = requireNonNull(repository, "repository");
    }

    @Override
    public void openAccount(final Account.Id accountId, final CurrencyUnit heldIn) {
        requireNonNull(accountId, "accountId");
        requireNonNull(heldIn, "heldIn");
        repository.put(new Account(accountId, heldIn));
    }
}
