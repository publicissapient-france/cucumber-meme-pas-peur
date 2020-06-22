package fr.esiha.katas.bank.account.domain;

import fr.esiha.katas.bank.account.domain.account.Account;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class UnknownAccountExceptionTest {
    @Test
    void should_be_final() {
        assertThat(UnknownAccountException.class).isFinal();
    }

    @Test
    void should_be_a_runtime_exception() {
        assertThat(RuntimeException.class).isAssignableFrom(UnknownAccountException.class);
    }

    @Test
    @SuppressWarnings("ThrowableNotThrown")
    void should_fail_to_create_from_null_account_id() {
        assertThatNullPointerException()
            .isThrownBy(() -> new UnknownAccountException(null))
            .withMessage("accountId");
    }

    @Test
    void should_have_readable_message() {
        final var lois = Account.Id.of("Lois");
        assertThat(new UnknownAccountException(lois))
            .hasMessage(format("There is no account with identifier %s.", lois));
    }
}
