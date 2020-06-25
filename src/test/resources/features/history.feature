Feature: Check history on a bank account.

    In order check the state of their bank accounts, bank clients want to retrieve a complete history.

    Scenario: Ada checks the history of her account.
        Given Ada has a newly opened bank account held in EUR
        And Ada has made operations on her account:
        * a deposit of EUR 400
        * a withdrawal of EUR 140
        * a deposit of EUR 33
        When Ada checks the history of her account
        Then she obtains a history that shows:
        * a balance of EUR 293
        * the first operation is a deposit of EUR 400
        * the second operation is a withdrawal of EUR 140
        * the third operation is a deposit of EUR 33
