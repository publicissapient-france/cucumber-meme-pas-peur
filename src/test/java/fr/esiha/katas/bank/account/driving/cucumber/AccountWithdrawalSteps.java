package fr.esiha.katas.bank.account.driving.cucumber;

import fr.esiha.katas.bank.account.domain.AccountWithdrawalService;
import io.cucumber.java8.En;

public class AccountWithdrawalSteps implements En {
    public AccountWithdrawalSteps(final AccountWithdrawalService accountWithdrawalService) {
        When("{accountId} withdraws {money} from his account", accountWithdrawalService::withdraw);
    }
}
