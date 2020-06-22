package fr.esiha.katas.bank.account.domain;

import fr.esiha.katas.bank.account.domain.account.Account;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

final class UnknownAccountException extends RuntimeException {
    UnknownAccountException(final Account.Id accountId) {
        super(format("There is no account with identifier %s.", requireNonNull(accountId, "accountId")));
    }
}
