package fr.esiha.katas.bank.account.domain;

import fr.esiha.katas.bank.account.domain.account.Account;
import org.joda.money.CurrencyUnit;

public interface AccountOpeningService {
    void openAccount(Account.Id accountId, CurrencyUnit heldIn);
}
