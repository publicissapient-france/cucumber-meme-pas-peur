package fr.esiha.katas.bank.account.domain.account;

import org.junit.jupiter.api.Test;

import static java.time.Instant.EPOCH;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.joda.money.CurrencyUnit.EUR;
import static org.joda.money.Money.zero;

public class HistoryTest {
    @Test
    void should_be_final() {
        assertThat(History.class).isFinal();
    }

    @Test
    void should_fail_to_create_from_null_timestamp() {
        assertThatNullPointerException()
            .isThrownBy(() -> History.of(null, zero(EUR), emptyList()))
            .withMessage("timestamp");
    }

    @Test
    void should_fail_to_create_from_null_money() {
        assertThatNullPointerException()
            .isThrownBy(() -> History.of(EPOCH, null, emptyList()))
            .withMessage("balance");
    }

    @Test
    void should_fail_to_create_from_null_list() {
        assertThatNullPointerException()
            .isThrownBy(() -> History.of(EPOCH, zero(EUR), null))
            .withMessage("operations");
    }

    @Test
    void should_compare_equal_to_another_created_with_same_parameters() {
        assertThat(History.of(EPOCH, zero(EUR), emptyList()))
            .isNotNull()
            .isEqualTo(History.of(EPOCH, zero(EUR), emptyList()));
    }

    @Test
    void should_have_readable_string_representation() {
        assertThat(History.of(EPOCH, zero(EUR), emptyList()).toString())
            .contains("History", EPOCH.toString(), zero(EUR).toString());

    }
}
