package fr.esiha.katas.bank.account.domain.account;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static fr.esiha.katas.bank.account.domain.account.Operation.depositOf;
import static fr.esiha.katas.bank.account.domain.account.Operation.withdrawalOf;
import static java.time.Instant.EPOCH;
import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.joda.money.CurrencyUnit.EUR;
import static org.joda.money.CurrencyUnit.USD;
import static org.joda.money.Money.zero;

public class AccountTest {
    private static Account anAccount(final CurrencyUnit heldIn) {
        return new Account(Account.Id.of("John Doe"), heldIn);
    }

    @Test
    void should_be_final() {
        assertThat(Account.class).isFinal();
    }

    @Test
    void should_fail_to_create_from_null_id() {
        assertThatNullPointerException()
            .isThrownBy(() -> new Account(null, EUR))
            .withMessage("id");
    }

    @Test
    void should_fail_to_create_from_null_currency() {
        assertThatNullPointerException()
            .isThrownBy(() -> new Account(Account.Id.of("Charlie"), null))
            .withMessage("heldIn");
    }

    @Test
    void should_compare_equal_to_another_created_with_same_id() {
        final var id = Account.Id.of("Charlie");
        assertThat(new Account(id, EUR))
            .isEqualTo(new Account(id, USD));
    }

    @Test
    void should_have_a_balance_of_zero_when_created() {
        assertThat(anAccount(EUR).getBalance()).isEqualTo(zero(EUR));
    }

    @Test
    void should_have_no_operations_when_created() {
        assertThat(anAccount(USD).getOperations()).isEmpty();
    }

    @Nested
    class AccountDepositTest {
        @Test
        void should_fail_to_deposit_with_null_money() {
            assertThatNullPointerException()
                .isThrownBy(() -> anAccount(EUR).deposit(null, EPOCH))
                .withMessage("depositAmount");
        }

        @Test
        void should_fail_to_deposit_with_null_instant() {
            assertThatNullPointerException()
                .isThrownBy(() -> anAccount(EUR).deposit(zero(EUR), null))
                .withMessage("timestamp");
        }

        @Test
        void should_deposit_valid_amount() {
            final var account = anAccount(EUR);
            final var depositAmount = Money.of(EUR, 250);

            account.deposit(depositAmount, EPOCH);

            assertSoftly(softly -> {
                final var operation = depositOf(depositAmount, EPOCH);
                softly.assertThat(account.getBalance()).isEqualTo(operation.affectBalance(zero(EUR)));
                softly.assertThat(account.getOperations()).endsWith(operation);
            });
        }
    }

    @Nested
    class AccountWithdrawalTest {
        @Test
        void should_fail_to_withdraw_with_null_money() {
            assertThatNullPointerException()
                .isThrownBy(() -> anAccount(EUR).withdraw(null, EPOCH))
                .withMessage("withdrawalAmount");
        }

        @Test
        void should_fail_to_withdraw_with_null_instant() {
            assertThatNullPointerException()
                .isThrownBy(() -> anAccount(EUR).withdraw(zero(EUR), null))
                .withMessage("timestamp");
        }

        @Test
        void should_withdraw_valid_amount() {
            final var account = anAccount(EUR);
            final var withdrawalAmount = Money.of(EUR, 250);

            account.withdraw(withdrawalAmount, EPOCH);

            assertSoftly(softly -> {
                final var operation = withdrawalOf(withdrawalAmount, EPOCH);
                softly.assertThat(account.getBalance()).isEqualTo(operation.affectBalance(zero(EUR)));
                softly.assertThat(account.getOperations()).endsWith(operation);
            });
        }
    }

    @Nested
    class AccountHistoryTest {
        @Test
        void should_fail_to_generate_history_with_null_instant() {
            assertThatNullPointerException()
                .isThrownBy(() -> anAccount(USD).generateHistory(null))
                .withMessage("timestamp");
        }

        @Test
        void should_create_history_with_account_information() {
            final var account = anAccount(USD);
            account.deposit(Money.of(USD, 40), EPOCH);
            account.withdraw(Money.of(USD, 3), EPOCH.plus(Duration.ofDays(3)));
            final var now = now();

            assertThat(account.generateHistory(now))
                .isEqualTo(History.of(
                    now,
                    Money.of(USD, 37),
                    List.of(
                        depositOf(Money.of(USD, 40), EPOCH),
                        withdrawalOf(Money.of(USD, 3), EPOCH.plus(Duration.ofDays(3)))
                    )
                ));
        }
    }

    @Nested
    class AccountBalanceTest {
        @Test
        void should_fail_to_tell_it_has_at_least_balance_of_with_null_money() {
            assertThatNullPointerException()
                .isThrownBy(() -> anAccount(EUR).hasBalanceOfAtLeast(null))
                .withMessage("amount");
        }

        @Test
        void should_tell_it_has_at_least_balance_of_equal_money() {
            final var account = anAccount(EUR);
            final var amount = Money.of(EUR, 123);
            account.deposit(amount, now());

            assertThat(account.hasBalanceOfAtLeast(amount)).isTrue();
        }

        @Test
        void should_tell_it_does_not_have_at_least_balance_of_greater_money() {
            final var account = anAccount(EUR);
            account.deposit(Money.of(EUR, 100), now());

            assertThat(account.hasBalanceOfAtLeast(Money.of(EUR, 101))).isFalse();
        }

        @Test
        void should_tell_it_has_at_least_balance_of_lesser_money() {
            final var account = anAccount(EUR);
            account.deposit(Money.of(EUR, 100), now());

            assertThat(account.hasBalanceOfAtLeast(Money.of(EUR, 99))).isTrue();
        }
    }

    @Nested
    class IdTest {
        @Test
        void should_be_final() {
            assertThat(Account.Id.class).isFinal();
        }

        @Test
        @SuppressWarnings("ResultOfMethodCallIgnored")
        void should_fail_to_create_from_null_string() {
            assertThatNullPointerException()
                .isThrownBy(() -> Account.Id.of(null))
                .withMessage("value");
        }

        @Test
        void should_compare_equal_to_another_created_with_same_parameter() {
            assertThat(Account.Id.of("Alice"))
                .isNotNull()
                .isEqualTo(Account.Id.of("Alice"));
        }

        @Test
        void should_have_readable_string_representation() {
            assertThat(Account.Id.of("Bob"))
                .hasToString("AccountId(Bob)");
        }
    }
}
