package fr.esiha.katas.bank.account.driving.cucumber;

import fr.esiha.katas.bank.account.domain.account.Operation;
import io.cucumber.java8.En;
import org.joda.money.Money;

import java.util.function.Predicate;

import static fr.esiha.katas.bank.account.driving.cucumber.JodaMoneySteps.MONEY_REGEX;

public class OperationSteps implements En {
    public OperationSteps() {
        ParameterType("operationPredicate", "(deposit|withdrawal) of (" + MONEY_REGEX + ")", this::parseOperationPredicate);
    }

    private Predicate<Operation> parseOperationPredicate(final String type, final String amountText) {
        final Predicate<Operation> typePredicate =  "deposit".equals(type)
            ? Operation::isDeposit
            : Operation::isWithdrawal;
        return typePredicate.and(operation -> operation.getAmount().equals(Money.parse(amountText)));
    }
}
