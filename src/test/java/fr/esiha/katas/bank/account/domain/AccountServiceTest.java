package fr.esiha.katas.bank.account.domain;

import fr.esiha.katas.bank.account.domain.account.Account;
import fr.esiha.katas.bank.account.driven.inmemory.InMemoryAccountRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.joda.money.CurrencyUnit.EUR;
import static org.joda.money.CurrencyUnit.USD;

public class AccountServiceTest {
    private final AccountRepository accountRepository = new InMemoryAccountRepository();
    private final AccountService accountService = new AccountService(accountRepository);

    @Test
    void should_be_final() {
        assertThat(AccountService.class).isFinal();
    }

    @Test
    void should_be_an_account_opening_service() {
        assertThat(AccountOpeningService.class).isAssignableFrom(AccountService.class);
    }

    @Test
    void should_fail_to_create_from_null_account_repository() {
        assertThatNullPointerException()
            .isThrownBy(() -> new AccountService(null))
            .withMessage("repository");
    }

    @Test
    void should_fail_to_open_account_with_null_account_id() {
        assertThatNullPointerException()
            .isThrownBy(() -> accountService.openAccount(null, EUR))
            .withMessage("accountId");
    }

    @Test
    void should_fail_to_open_account_with_null_currency_unit() {
        assertThatNullPointerException()
            .isThrownBy(() -> accountService.openAccount(Account.Id.of("Germain"), null))
            .withMessage("heldIn");
    }

    @Test
    void should_open_new_account() {
        final var germain = Account.Id.of("Germain");
        accountService.openAccount(germain, USD);

        assertThat(accountRepository.get(germain))
            .contains(new Account(germain, USD));
    }
}
