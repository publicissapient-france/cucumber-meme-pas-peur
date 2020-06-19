package fr.esiha.katas.bank.account.driven.inmemory;

import fr.esiha.katas.bank.account.domain.AccountRepository;
import fr.esiha.katas.bank.account.domain.account.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public final class InMemoryAccountRepository implements AccountRepository {
    private final Map<Account.Id, Account> accounts = new HashMap<>();

    @Override
    public void put(final Account account) {
        requireNonNull(account, "account");
        accounts.put(account.getId(), account);
    }

    @Override
    public Optional<Account> get(final Account.Id accountId) {
        requireNonNull(accountId, "accountId");
        return ofNullable(accounts.get(accountId));
    }
}
