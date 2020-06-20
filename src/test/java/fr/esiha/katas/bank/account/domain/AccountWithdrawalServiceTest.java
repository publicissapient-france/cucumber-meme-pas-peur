package fr.esiha.katas.bank.account.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountWithdrawalServiceTest {
    @Test
    void should_be_an_interface() {
        assertThat(AccountWithdrawalService.class).isInterface();
    }

    @Test
    void should_declare_method() {
        assertThat(AccountWithdrawalService.class).hasDeclaredMethods("withdraw");
    }
}
