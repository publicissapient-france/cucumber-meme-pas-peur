package fr.esiha.katas.bank.account.domain;

import fr.esiha.katas.bank.account.domain.account.Account;
import fr.esiha.katas.bank.account.driven.inmemory.InMemoryAccountRepository;
import org.joda.money.Money;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneId;

import static fr.esiha.katas.bank.account.domain.account.Operation.depositOf;
import static fr.esiha.katas.bank.account.domain.account.Operation.withdrawalOf;
import static java.time.Clock.fixed;
import static java.time.Clock.systemUTC;
import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.*;
import static org.joda.money.CurrencyUnit.EUR;
import static org.joda.money.CurrencyUnit.USD;
import static org.joda.money.Money.zero;

public class AccountServiceTest {
    private final AccountRepository accountRepository = new InMemoryAccountRepository();
    private final Clock clock = fixed(now(), ZoneId.of("Europe/Paris"));
    private final AccountService accountService = new AccountService(accountRepository, clock);

    @Test
    void should_be_final() {
        assertThat(AccountService.class).isFinal();
    }

    @Test
    void should_fail_to_create_from_null_account_repository() {
        assertThatNullPointerException()
            .isThrownBy(() -> new AccountService(null, systemUTC()))
            .withMessage("repository");
    }

    @Test
    void should_fail_to_create_from_null_clock() {
        assertThatNullPointerException()
            .isThrownBy(() -> new AccountService(accountRepository, null))
            .withMessage("clock");
    }

    @Nested
    class AccountOpeningServiceTest {
        @Test
        void should_be_an_account_opening_service() {
            assertThat(AccountOpeningService.class).isAssignableFrom(AccountService.class);
        }

        @Test
        void should_fail_to_open_account_with_null_account_id() {
            assertThatNullPointerException()
                .isThrownBy(() -> accountService.openAccount(null, EUR))
                .withMessage("accountId");
        }

        @Test
        void should_fail_to_open_account_with_null_currency_unit() {
            assertThatNullPointerException()
                .isThrownBy(() -> accountService.openAccount(Account.Id.of("Germain"), null))
                .withMessage("heldIn");
        }

        @Test
        void should_open_new_account() {
            final var germain = Account.Id.of("Germain");
            accountService.openAccount(germain, USD);

            assertThat(accountRepository.get(germain))
                .contains(new Account(germain, USD));
        }
    }

    @Nested
    class AccountDepositServiceTest {
        @Test
        void should_be_an_account_deposit_service() {
            assertThat(AccountDepositService.class).isAssignableFrom(AccountService.class);
        }

        @Test
        void should_fail_to_deposit_with_null_account_id() {
            assertThatNullPointerException()
                .isThrownBy(() -> accountService.deposit(null, zero(EUR)))
                .withMessage("accountId");
        }

        @Test
        void should_fail_to_deposit_with_null_money() {
            assertThatNullPointerException()
                .isThrownBy(() -> accountService.deposit(Account.Id.of("Harry"), null))
                .withMessage("depositAmount");
        }

        @Test
        void should_fail_to_deposit_on_unknown_account() {
            final var accountId = Account.Id.of("Unknown");
            assertThatExceptionOfType(UnknownAccountException.class)
                .isThrownBy(() -> accountService.deposit(accountId, Money.of(EUR, 300)))
                .withMessageContaining(accountId.toString());
        }

        @Test
        void should_deposit_amount_on_correct_account_at_current_time() {
            final var accountId = Account.Id.of("Fred");
            accountRepository.put(new Account(accountId, EUR));

            accountService.deposit(accountId, Money.of(EUR, 250));

            assertThat(accountRepository.get(accountId).orElseThrow().getOperations())
                .endsWith(depositOf(Money.of(EUR, 250), clock.instant()));
        }
    }

    @Nested
    class AccountWithdrawalServiceTest {
        @Test
        void should_be_an_account_withdrawal_service() {
            assertThat(AccountWithdrawalService.class).isAssignableFrom(AccountService.class);
        }

        @Test
        void should_fail_to_withdraw_from_null_account_id() {
            assertThatNullPointerException()
                .isThrownBy(() -> accountService.withdraw(null, zero(EUR)))
                .withMessage("accountId");
        }

        @Test
        void should_fail_to_withdraw_with_null_money() {
            assertThatNullPointerException()
                .isThrownBy(() -> accountService.withdraw(Account.Id.of("Ines"), null))
                .withMessage("withdrawalAmount");
        }

        @Test
        void should_fail_to_withdraw_from_unknown_account() {
            final var accountId = Account.Id.of("Unknown");
            assertThatExceptionOfType(UnknownAccountException.class)
                .isThrownBy(() -> accountService.withdraw(accountId, zero(EUR)))
                .withMessageContaining(accountId.toString());
        }

        @Test
        void should_withdraw_amount_on_correct_account_at_current_time() {
            final var accountId = Account.Id.of("Kent");
            createAccount(accountId, Money.of(EUR, 500));

            accountService.withdraw(accountId, Money.of(EUR, 250));

            assertThat(accountRepository.get(accountId).orElseThrow().getOperations())
                .endsWith(withdrawalOf(Money.of(EUR, 250), clock.instant()));
        }

        @Test
        void should_fail_to_withdraw_more_than_the_account_balance() {
            final var accountId = Account.Id.of("Roger");
            final var balance = Money.of(EUR, 200);
            createAccount(accountId, balance);

            assertThatExceptionOfType(InsufficientProvisionException.class)
                .isThrownBy(() -> accountService.withdraw(accountId, Money.of(EUR, 1200)))
                .withMessageContaining(balance.toString());
        }

        private void createAccount(final Account.Id accountId, final Money initialDeposit) {
            accountRepository.put(new Account(accountId, initialDeposit.getCurrencyUnit()));
            accountService.deposit(accountId, initialDeposit);
        }
    }

    @Nested
    class AccountHistoryServiceTest {
        @Test
        void should_be_an_account_history_service() {
            assertThat(AccountHistoryService.class).isAssignableFrom(AccountService.class);
        }

        @Test
        void should_fail_to_get_account_history_of_null_account_id() {
            assertThatNullPointerException()
                .isThrownBy(() -> accountService.getAccountHistory(null))
                .withMessage("accountId");
        }

        @Test
        void should_fail_to_get_history_for_unknown_account() {
            final var accountId = Account.Id.of("Unknown");
            assertThatExceptionOfType(UnknownAccountException.class)
                .isThrownBy(() -> accountService.getAccountHistory(accountId))
                .withMessageContaining(accountId.toString());
        }

        @Test
        void should_generate_account_history_with_current_time() {
            final var accountId = Account.Id.of("Fred");
            final var account = new Account(accountId, EUR);
            accountRepository.put(account);

            assertThat(accountService.getAccountHistory(accountId))
                .isEqualTo(account.generateHistory(clock.instant()));
        }
    }
}
