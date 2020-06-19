package fr.esiha.katas.bank.account.domain.account;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static org.joda.money.Money.zero;

public final class Account {
    private final Id id;
    private final CurrencyUnit heldIn;

    public Account(final Id id, final CurrencyUnit heldIn) {
        this.id = requireNonNull(id, "id");
        this.heldIn = requireNonNull(heldIn, "heldIn");
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Money getBalance() {
        return zero(heldIn);
    }

    public List<Operation> getOperations() {
        return emptyList();
    }

    public Id getId() {
        return id;
    }

    public static final class Id {
        private final String value;

        private Id(final String value) {
            this.value = requireNonNull(value, "value");
        }

        public static Id of(final String value) {
            return new Id(value);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Id id = (Id) o;
            return Objects.equals(value, id.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return format("AccountId(%s)", value);
        }
    }
}
