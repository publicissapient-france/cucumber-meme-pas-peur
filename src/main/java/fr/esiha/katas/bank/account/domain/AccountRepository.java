package fr.esiha.katas.bank.account.domain;

import fr.esiha.katas.bank.account.domain.account.Account;

import java.util.Optional;

public interface AccountRepository {
    void put(Account account);

    Optional<Account> get(Account.Id accountId);
}
