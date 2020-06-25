Feature: Withdraw money from a bank account.

   In order to retrieve their savings, bank account clients can withdraw money from their bank account.

   Scenario: Roger withdraws part of his savings
      Given Roger has a bank account with a balance of EUR 1500
      When Roger withdraws EUR 250 from his account
      Then Roger's account has a balance of EUR 1250

   Scenario: Emma withdraws all of her savings
      Given Emma has a bank account with a balance of EUR 1500
      When Emma withdraws EUR 1500 from her account
      Then Emma's account has a balance of EUR 0
