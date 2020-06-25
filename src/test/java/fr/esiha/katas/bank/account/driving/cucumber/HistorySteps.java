package fr.esiha.katas.bank.account.driving.cucumber;

import fr.esiha.katas.bank.account.domain.account.History;
import fr.esiha.katas.bank.account.domain.account.Operation;
import io.cucumber.java8.En;
import org.joda.money.Money;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class HistorySteps implements En {
    private final AccountHistorySteps accountHistorySteps;
    private History lastHistory;

    public HistorySteps(final AccountHistorySteps accountHistorySteps) {
        this.accountHistorySteps = accountHistorySteps;
        declareSteps();
    }

    private void declareSteps() {
        ParameterType("index", "\\w+", this::parseIndex);
        Then("she obtains a history that shows:", this::retrieveLastHistory);
        Then("a balance of {money}", this::assertHasBalance);
        Then("the {index} operation is a {operationPredicate}", this::assertIndexedOperationMatches);
    }

    private int parseIndex(final String text) {
        final var index = List.of("first", "second", "third").indexOf(text);
        if (index < 0) {
            throw new IllegalArgumentException(text);
        }

        return index;
    }

    private void retrieveLastHistory() {
        lastHistory = accountHistorySteps.getLastHistory();
    }

    private void assertHasBalance(final Money balance) {
        assertThat(lastHistory.getBalance()).isEqualTo(balance);
    }

    private void assertIndexedOperationMatches(final int index, final Predicate<Operation> operationPredicate) {
        assertThat(lastHistory.getOperations())
            .element(index)
            .matches(operationPredicate);
    }
}
