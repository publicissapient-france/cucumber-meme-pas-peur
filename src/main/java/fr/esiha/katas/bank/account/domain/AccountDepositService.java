package fr.esiha.katas.bank.account.domain;

import fr.esiha.katas.bank.account.domain.account.Account;
import org.joda.money.Money;

public interface AccountDepositService {
    void deposit(Account.Id accountId, Money depositAmount);
}
