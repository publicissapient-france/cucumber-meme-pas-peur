package fr.esiha.katas.bank.account.driving.cucumber;

import fr.esiha.katas.bank.account.domain.AccountWithdrawalService;
import fr.esiha.katas.bank.account.domain.account.Account;
import io.cucumber.java8.En;
import org.joda.money.Money;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountWithdrawalSteps implements En {
    private final AccountWithdrawalService accountWithdrawalService;
    private final AccountSteps accountSteps;
    private boolean lastWithdrawalWasRefused;

    public AccountWithdrawalSteps(final AccountWithdrawalService accountWithdrawalService, final AccountSteps accountSteps) {
        this.accountWithdrawalService = accountWithdrawalService;
        this.accountSteps = accountSteps;
        declareSteps();
    }

    private void declareSteps() {
        When("{accountId} withdraws {money} from his/her account", this::withdraw);
        Then("the withdrawal is refused", this::assertLastWithdrawalWasRefused);
        When("a withdrawal of {money}", this::withdrawFromCurrentAccount);
    }

    private void withdraw(final Account.Id accountId, final Money money) {
        lastWithdrawalWasRefused = false;
        try {
            accountWithdrawalService.withdraw(accountId, money);
        } catch (final RuntimeException e) {
            lastWithdrawalWasRefused = true;
        }
    }

    private void assertLastWithdrawalWasRefused() {
        assertThat(lastWithdrawalWasRefused).isTrue();
    }

    private void withdrawFromCurrentAccount(final Money withdrawalAmount) {
        accountWithdrawalService.withdraw(accountSteps.getCurrentAccountId(), withdrawalAmount);
    }
}
