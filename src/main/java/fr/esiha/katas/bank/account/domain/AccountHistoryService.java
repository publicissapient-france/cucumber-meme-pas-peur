package fr.esiha.katas.bank.account.domain;

import fr.esiha.katas.bank.account.domain.account.Account;
import fr.esiha.katas.bank.account.domain.account.History;

public interface AccountHistoryService {
    History getAccountHistory(Account.Id accountId);
}
