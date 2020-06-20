package fr.esiha.katas.bank.account.domain;

import fr.esiha.katas.bank.account.domain.account.Account;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.time.Clock;

import static java.lang.String.format;
import static java.time.Clock.systemUTC;
import static java.util.Objects.requireNonNull;

public final class AccountService implements AccountOpeningService, AccountDepositService, AccountWithdrawalService {
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
        getAccount(accountId).deposit(depositAmount, clock.instant());
    }

    @Override
    public void withdraw(final Account.Id accountId, final Money withdrawalAmount) {
        requireNonNull(accountId, "accountId");
        requireNonNull(withdrawalAmount, "withdrawalAmount");
        getAccount(accountId).withdraw(withdrawalAmount, clock.instant());
    }

    private Account getAccount(final Account.Id accountId) {
        return repository.get(accountId).orElseThrow(() -> unknownAccount(accountId));
    }
}
