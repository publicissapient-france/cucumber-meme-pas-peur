package fr.esiha.katas.bank.account.domain.account;

import org.joda.money.Money;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import static java.util.List.copyOf;
import static java.util.Objects.requireNonNull;

public final class History {
    private final Instant timestamp;
    private final Money balance;
    private final List<Operation> operations;

    private History(final Instant timestamp, final Money balance, final List<Operation> operations) {
        this.timestamp = requireNonNull(timestamp, "timestamp");
        this.balance = requireNonNull(balance, "balance");
        this.operations = copyOf(requireNonNull(operations, "operations"));
    }

    public static History of(final Instant timestamp, final Money balance, final List<Operation> operations) {
        return new History(timestamp, balance, operations);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final History history = (History) o;
        return Objects.equals(timestamp, history.timestamp) &&
            Objects.equals(balance, history.balance) &&
            Objects.equals(operations, history.operations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, balance, operations);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", History.class.getSimpleName() + "[", "]")
            .add("timestamp=" + timestamp)
            .add("balance=" + balance)
            .add("operations=" + operations)
            .toString();
    }

    public Money getBalance() {
        return balance;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}
