package fr.esiha.katas.bank.account.domain;

import org.joda.money.Money;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.joda.money.CurrencyUnit.EUR;

public class InsufficientProvisionExceptionTest {
    @Test
    void should_be_final() {
        assertThat(InsufficientProvisionException.class).isFinal();
    }

    @Test
    void should_be_a_runtime_exception() {
        assertThat(RuntimeException.class).isAssignableFrom(InsufficientProvisionException.class);
    }

    @Test
    @SuppressWarnings("ThrowableNotThrown")
    void should_fail_to_create_from_null_money() {
        assertThatNullPointerException()
            .isThrownBy(() -> new InsufficientProvisionException(null))
            .withMessage("balance");
    }

    @Test
    void should_have_readable_message() {
        final var balance = Money.of(EUR, 123);
        assertThat(new InsufficientProvisionException(balance))
            .hasMessage(format("Balance of %s is insufficient.", balance));
    }
}
