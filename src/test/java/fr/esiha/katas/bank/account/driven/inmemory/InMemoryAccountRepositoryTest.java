package fr.esiha.katas.bank.account.driven.inmemory;

import fr.esiha.katas.bank.account.domain.AccountRepository;
import fr.esiha.katas.bank.account.domain.account.Account;
import org.joda.money.CurrencyUnit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class InMemoryAccountRepositoryTest {

    private final InMemoryAccountRepository repository = new InMemoryAccountRepository();

    @Test
    void should_be_final() {
        assertThat(InMemoryAccountRepository.class).isFinal();
    }

    @Test
    void should_be_an_account_repository() {
        assertThat(AccountRepository.class).isAssignableFrom(InMemoryAccountRepository.class);
    }

    @Test
    void should_fail_to_put_null_account() {
        assertThatNullPointerException()
            .isThrownBy(() -> repository.put(null))
            .withMessage("account");
    }

    @Test
    void should_fail_to_get_with_null_account_id() {
        assertThatNullPointerException()
            .isThrownBy(() -> repository.get(null))
            .withMessage("accountId");
    }

    @Test
    void should_return_empty_when_no_account_has_given_id() {
        assertThat(repository.get(Account.Id.of("Dawn")))
            .isEmpty();
    }

    @Test
    void should_get_previously_saved_account() {
        final var edgarAccount = new Account(Account.Id.of("Edgar"), CurrencyUnit.EUR);
        final var flintAccount = new Account(Account.Id.of("Flint"), CurrencyUnit.USD);
        repository.put(edgarAccount);
        repository.put(flintAccount);

        assertSoftly(softly -> {
            softly.assertThat(repository.get(Account.Id.of("Edgar"))).contains(edgarAccount);
            softly.assertThat(repository.get(Account.Id.of("Flint"))).contains(flintAccount);
        });
    }
}
