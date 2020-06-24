Feature: Deposit money on a bank account.

    In order to save money, bank clients want to deposit money on their account.

    Scenario: Karen deposits money on her account for the first time
        Given Karen has a newly opened bank account held in EUR
        When Karen deposits EUR 123.45 on her account
        Then Karen's account has a balance of EUR 123.45
