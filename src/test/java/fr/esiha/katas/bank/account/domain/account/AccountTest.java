package fr.esiha.katas.bank.account.domain.account;

import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
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
