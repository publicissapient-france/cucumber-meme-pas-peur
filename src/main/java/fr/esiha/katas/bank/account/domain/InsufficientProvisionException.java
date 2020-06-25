package fr.esiha.katas.bank.account.domain;

import org.joda.money.Money;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

final class InsufficientProvisionException extends RuntimeException {
    public InsufficientProvisionException(final Money balance) {
        super(format("Balance of %s is insufficient.", requireNonNull(balance, "balance")));
    }
}
