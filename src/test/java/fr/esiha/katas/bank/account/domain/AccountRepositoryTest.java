package fr.esiha.katas.bank.account.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountRepositoryTest {
    @Test
    void should_be_an_interface() {
        assertThat(AccountRepository.class).isInterface();
    }

    @Test
    void should_define_methods() {
        assertThat(AccountRepository.class).hasDeclaredMethods("put", "get");
    }
}
