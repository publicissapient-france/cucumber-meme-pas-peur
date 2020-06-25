Feature: Deposit money on a bank account.

    In order to save money, bank clients want to deposit money on their account.

    Background:
        Given Karen has a newly opened bank account held in EUR

    Scenario: Karen deposits money on her account for the first time
        When Karen deposits EUR 123.45 on her account
        Then Karen's account has a balance of EUR 123.45

    Scenario: Karen deposits money on her account several times
        Given Karen has deposited EUR 350 on her account
        When Karen deposits EUR 500 on her account
        Then Karen's account has a balance of EUR 850
