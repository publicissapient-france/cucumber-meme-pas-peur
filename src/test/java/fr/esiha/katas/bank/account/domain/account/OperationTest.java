package fr.esiha.katas.bank.account.domain.account;

import org.joda.money.Money;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static java.lang.reflect.Modifier.isPrivate;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.*;
import static org.joda.money.CurrencyUnit.EUR;
import static org.joda.money.Money.zero;

class OperationTest {
    private static Money euros(final int value) {
        return Money.of(EUR, value);
    }

    @Test
    void should_be_abstract() {
        assertThat(Operation.class).isAbstract();
    }

    @Test
    void should_be_sealed() {
        assertThat(Operation.class.getDeclaredConstructors())
            .allMatch(constructor -> isPrivate(constructor.getModifiers()));
    }

    @Nested
    class WithdrawalTest {
        @Test
        void should_fail_to_create_from_null_money() {
            assertThatNullPointerException()
                .isThrownBy(() -> Operation.withdrawalOf(null, EPOCH))
                .withMessage("amount");
        }

        @Test
        void should_fail_to_create_from_negative_money() {
            assertThatIllegalArgumentException()
                .isThrownBy(() -> Operation.withdrawalOf(euros(-1), EPOCH))
                .withMessage("Amount must be greater than or equal to zero.");
        }

        @Test
        void should_fail_to_create_from_null_instant() {
            assertThatNullPointerException()
                .isThrownBy(() -> Operation.withdrawalOf(zero(EUR), null))
                .withMessage("timestamp");
        }

        @Test
        void should_compare_equal_to_another_created_with_same_parameters() {
            assertThat(Operation.withdrawalOf(zero(EUR), EPOCH))
                .isNotNull()
                .isEqualTo(Operation.withdrawalOf(zero(EUR), EPOCH));
        }

        @Test
        void should_have_readable_string_representation() {
            final var amount = euros(5);
            final var timestamp = EPOCH;
            assertThat(Operation.withdrawalOf(amount, timestamp))
                .hasToString(format("Withdrawal(%s,%s)", amount, timestamp));
        }

        @Test
        void should_be_a_withdrawal() {
            assertThat(Operation.withdrawalOf(euros(3), EPOCH).isWithdrawal()).isTrue();
        }

        @Test
        void should_not_be_a_deposit() {
            assertThat(Operation.withdrawalOf(euros(3), EPOCH).isDeposit()).isFalse();
        }

        @Test
        void should_fail_to_affect_balance_with_null_money() {
            assertThatNullPointerException()
                .isThrownBy(() -> Operation.withdrawalOf(euros(5), EPOCH).affectBalance(null))
                .withMessage("balance");
        }

        @Test
        void should_affect_balance_by_subtracting_amount() {
            assertThat(Operation.withdrawalOf(euros(5), EPOCH).affectBalance(euros(18)))
                .isEqualTo(euros(18).minus(euros(5)));
        }
    }

    @Nested
    class DepositTest {
        @Test
        void should_fail_to_create_from_null_money() {
            assertThatNullPointerException()
                .isThrownBy(() -> Operation.depositOf(null, EPOCH))
                .withMessage("amount");
        }

        @Test
        void should_fail_to_create_with_negative_money() {
            assertThatIllegalArgumentException()
                .isThrownBy(() -> Operation.depositOf(euros(-1), EPOCH))
                .withMessage("Amount must be greater than or equal to zero.");
        }

        @Test
        void should_fail_to_create_from_null_instant() {
            assertThatNullPointerException()
                .isThrownBy(() -> Operation.depositOf(zero(EUR), null))
                .withMessage("timestamp");
        }

        @Test
        void should_compare_equal_to_another_created_with_same_parameters() {
            assertThat(Operation.depositOf(euros(5), EPOCH))
                .isNotNull()
                .isEqualTo(Operation.depositOf(euros(5), EPOCH));
        }

        @Test
        void should_have_readable_string_representation() {
            final var amount = euros(7);
            final var timestamp = EPOCH;
            assertThat(Operation.depositOf(amount, timestamp))
                .hasToString(format("Deposit(%s,%s)", amount, timestamp));
        }

        @Test
        void should_not_be_a_withdrawal() {
            assertThat(Operation.depositOf(euros(3), EPOCH).isWithdrawal()).isFalse();
        }

        @Test
        void should_be_a_deposit() {
            assertThat(Operation.depositOf(euros(3), EPOCH).isDeposit()).isTrue();
        }

        @Test
        void should_fail_to_affect_balance_with_null_money() {
            assertThatNullPointerException()
                .isThrownBy(() -> Operation.depositOf(euros(2), EPOCH).affectBalance(null))
                .withMessage("balance");
        }

        @Test
        void should_affect_balance_by_adding_amount() {
            assertThat(Operation.depositOf(euros(3), EPOCH).affectBalance(euros(10)))
                .isEqualTo(euros(3).plus(euros(10)));
        }
    }
}
