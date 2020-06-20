package fr.esiha.katas.bank.account.domain;
import fr.esiha.katas.bank.account.domain.account.Account;
import fr.esiha.katas.bank.account.driven.inmemory.InMemoryAccountRepository;
import org.joda.money.Money;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneId;

import static fr.esiha.katas.bank.account.domain.account.Operation.depositOf;
import static java.lang.String.format;
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
            assertThatIllegalArgumentException()
                .isThrownBy(() -> accountService.deposit(accountId, Money.of(EUR, 300)))
                .withMessage(format("There is no account with identifier %s.", accountId));
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
}
