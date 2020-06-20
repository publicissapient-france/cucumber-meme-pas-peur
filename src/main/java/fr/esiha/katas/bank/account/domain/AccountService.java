package fr.esiha.katas.bank.account.domain;

import fr.esiha.katas.bank.account.domain.account.Account;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.time.Clock;

import static java.lang.String.format;
import static java.time.Clock.systemUTC;
import static java.util.Objects.requireNonNull;

public final class AccountService implements AccountOpeningService, AccountDepositService {
    private final AccountRepository repository;
    private final Clock clock;

    public AccountService(final AccountRepository repository) {
        this(repository, systemUTC());
    }

    AccountService(final AccountRepository repository, final Clock clock) {
        this.repository = requireNonNull(repository, "repository");
        this.clock = requireNonNull(clock, "clock");
    }

    private static IllegalArgumentException unknownAccount(final Account.Id accountId) {
        return new IllegalArgumentException(format("There is no account with identifier %s.", accountId));
    }

    @Override
    public void openAccount(final Account.Id accountId, final CurrencyUnit heldIn) {
        requireNonNull(accountId, "accountId");
        requireNonNull(heldIn, "heldIn");
        repository.put(new Account(accountId, heldIn));
    }

    @Override
    public void deposit(final Account.Id accountId, final Money depositAmount) {
        requireNonNull(accountId, "accountId");
        requireNonNull(depositAmount, "depositAmount");
        final var account = repository.get(accountId).orElseThrow(() -> unknownAccount(accountId));
        account.deposit(depositAmount, clock.instant());
    }
}
