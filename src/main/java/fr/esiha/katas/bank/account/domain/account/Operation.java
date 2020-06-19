package fr.esiha.katas.bank.account.domain.account;

import org.joda.money.Money;

import java.time.Instant;
import java.util.Objects;
import java.util.function.BinaryOperator;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public abstract class Operation {
    private final Money amount;
    private final Instant timestamp;

    private Operation(final Money amount, final Instant timestamp) {
        this.amount = requireNonNegative(requireNonNull(amount, "amount"));
        this.timestamp = requireNonNull(timestamp, "timestamp");
    }

    private static Money requireNonNegative(final Money amount) {
        if (amount.isNegative()) {
            throw new IllegalArgumentException("Amount must be greater than or equal to zero.");
        }
        return amount;
    }

    public static Operation withdrawalOf(final Money amount, final Instant timestamp) {
        return new Withdrawal(amount, timestamp);
    }

    public static Operation depositOf(final Money amount, final Instant timestamp) {
        return new Deposit(amount, timestamp);
    }

    public final Money affectBalance(final Money balance) {
        requireNonNull(balance, "balance");
        return balanceAffectingFunction().apply(balance, amount);
    }

    abstract BinaryOperator<Money> balanceAffectingFunction();

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Operation operation = (Operation) o;
        return Objects.equals(amount, operation.amount) &&
            Objects.equals(timestamp, operation.timestamp);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(amount, timestamp);
    }

    @Override
    public final String toString() {
        return format("%s(%s,%s)", getClass().getSimpleName(), amount, timestamp);
    }

    private static final class Withdrawal extends Operation {
        Withdrawal(final Money amount, final Instant timestamp) {
            super(amount, timestamp);
        }

        @Override
        BinaryOperator<Money> balanceAffectingFunction() {
            return Money::minus;
        }
    }

    private static final class Deposit extends Operation {
        Deposit(final Money amount, final Instant timestamp) {
            super(amount, timestamp);
        }

        @Override
        BinaryOperator<Money> balanceAffectingFunction() {
            return Money::plus;
        }
    }
}
