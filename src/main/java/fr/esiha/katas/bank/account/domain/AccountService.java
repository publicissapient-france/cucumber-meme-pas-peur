package fr.esiha.katas.bank.account.domain;

import fr.esiha.katas.bank.account.domain.account.Account;
import fr.esiha.katas.bank.account.domain.account.History;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.time.Clock;

import static java.time.Clock.systemUTC;
import static java.util.Objects.requireNonNull;

public final class AccountService implements AccountOpeningService, AccountDepositService, AccountWithdrawalService, AccountHistoryService {
    private final AccountRepository repository;
    private final Clock clock;

    public AccountService(final AccountRepository repository) {
        this(repository, systemUTC());
    }

    AccountService(final AccountRepository repository, final Clock clock) {
        this.repository = requireNonNull(repository, "repository");
        this.clock = requireNonNull(clock, "clock");
    }

    private static void requireSufficientProvision(final Account account, final Money withdrawalAmount) {
        if (!account.hasBalanceOfAtLeast(withdrawalAmount)) {
            throw new InsufficientProvisionException(account.getBalance());
        }
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

        final var account = getAccount(accountId);
        requireSufficientProvision(account, withdrawalAmount);
        account.withdraw(withdrawalAmount, clock.instant());
    }

    @Override
    public History getAccountHistory(final Account.Id accountId) {
        requireNonNull(accountId, "accountId");
        return getAccount(accountId).generateHistory(clock.instant());
    }

    private Account getAccount(final Account.Id accountId) {
        return repository.get(accountId).orElseThrow(() -> new UnknownAccountException(accountId));
    }
}
