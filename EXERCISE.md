# Exercices d'introduction à Cucumber

> Gardez un onglet de navigateur ouvert à l'adresse https://cucumber.io/docs/cucumber/ . Vous en aurez bien besoin :)

## Ajout des dépendances

Pour avancer dans les exercices, il faut d'abord ajouter quelques dépendances, en scope `test`.

### Cucumber

On utilisera la version `5.6.0` de Cucumber pour spécifier les artéfacts :
* `io.cucumber:cucumber-java8`, qui apporte les interfaces qu'on va implémenter pour créer le code de glue entre les
   scénarios Gherkin et le Domaine.
* `io.cucumber:cucumber-junit`, qui apporte le Runner JUnit4 pour lancer les tests du Domaine. Cucumber n'est pas
   compatible directement avec JUnit5, on verra juste après comment régler ce problème.
* `io.cucumber:cucumber-picocontainer`, qui apporte le support de l'injection de dépendances avec PicoContainer, on va
   s'en servir pour assembler l'application qui sera soumise aux tests du Domaine.

### JUnit

Le Kata utilise JUnit5, mais Cucumber a besoin de JUnit4. Pas de problème, JUnit5 propose un moteur nommé Vintage pour
faire tourner les tests JUnit4 avec JUnit5. On a besoin des dépendances suivantes :

* `org.junit.vintage:junit-vintage-engine:${junit5.version}`, qui apporte le moteur Vintage.
* `junit:junit:4.13`, qui apporte JUnit4.

## Mise en place des tests du Domaine

### Lanceur JUnit

1. Créer une classe de test `fr.esiha.katas.bank.account.DomainTest`, à lancer avec le Runner JUnit4
   `io.cucumber.junit.Cucumber`.
2. Configurer la class de test avec l'annotation `io.cucumber.junit.CucumberOptions` pour :
    * Activer le mode `strict` ;
    * Indiquer que les `features` seront à trouver dans `"classpath:features"` (on les créera dans le dossier
      `src/test/resources/features`) ;
    * Utiliser le `plugin` de sortie `"pretty"` ;
    * Optionnellement, filtrer les scénarios pour ne lancer que ceux qui répondent à la condition de `tags` définie à
      `"not @unimplemented"` ;
3. Lancer la classe de test `DomainTest`, Cucumber devrait avertir qu'il n'a pas trouvé de scénarios.

### Préparation de l'injection de dépendances

Pour réconcilier le code et les spécifications, il va falloir monter une application avec des Adaptateurs branchés aux
Ports exposés par le Domaine de notre Hexagone.

Côté Driven, il n'y a qu'un seul Port : `AccountRepository`. Il y a un Adaptateur In-Memory pour ce port, implémenté par
`InMemoryAccountRepository`. Ça va être parfait pour notre besoin.

Côté Driving, il ya 4 Ports : `AccountDepositService`, `AccountHistoryService`, `AccountOpeningService` et
`AccountWithdrawalService`. Tous ces Ports sont implémentés par `AccountService`, c'est lui qu'on voudra injecter comme
instance à l'Adaptateur Driving qu'on va créer pour Cucumber.

Avec la dépendance `cucumber-picocontainer`, on a un conteneur d'injection de dépendances très léger auquel il ne manque
qu'une seule fonctionnalité : la découverte des implémentations à utiliser pour satisfaire des dépendances sur des
interfaces. Mettons tout ça en place.

1. Créer une classe imbriquée (`static`) dans `DomainTest`, nommée `Configuration` :
    * Implémentant `io.cucumber.core.backend.ObjectFactory` ;
    * Déléguant tout à une `io.cucumber.picocontainer.PicoFactory` créée dans le constructeur.
2. Enregistrer notre `ObjectFactory` dans le système SPI de Java en créant un fichier
   `src/test/resources/META-INF/services/io.cucumber.core.backend.ObjectFactory` contenant :
    ```
    fr.esiha.katas.bank.account.DomainTest$Configuration
    ```
3. Modifier les paramètres de `@CucumberOptions` pour lui designer `DomainTest.Configuration.class` comme `objectFactory`
   à utiliser.
4. Enregistrer auprès du conteneur d'injection de dépendances les classes `AccountService` (qui implémente les Ports
   Driving) et `InMemoryAccountRepository` (qui implémente le seul Port Driven) :
    ```java
    public Configuration() {
        this.delegate = new PicoFactory();
        this.delegate.addClass(AccountService.class);
        this.delegate.addClass(InMemoryAccountRepository.class);
    }
    ```

## Rédaction et implémentation des Scénarios

### Pour la fonctionnalité « Deposit »

On entre dans le vif du sujet : rédiger un fichier de feature, au format Gherkin, et créer le code de glue qui va
permettre de vérifier l'adéquation entre les spécifications et la réalité de la production.

1. Créer le fichier `src/test/resources/features/deposit.feature` avec le contenu suivant :
    ```gherkin
    Feature: Deposit money on a bank account.

        In order to save money, bank clients want to deposit money on their account.

        Scenario: Karen deposits money on her account for the first time
          Given Karen has a newly opened bank account held in EUR
          When Karen deposits EUR 123.45 on her account
          Then Karen's account has a balance of EUR 123.45
    ```
2. Lancer à nouveau la classe de test `DomainTest`. Cucumber devrait se plaindre que 3 étapes (Steps) ne sont pas
   définies. Une étape est une phrase de la syntaxe Gherkin.
3. Commencer l'implémentation de l'Adaptateur Driving pour Cucumber avec la classe de test
   `fr.esiha.katas.bank.account.driving.cucumber.AccountOpeningSteps`:
    * Elle doit implémenter `io.cucumber.java8.En` ;
    * Ajouter un paramètre au constructeur, de type `AccountOpeningService` (notre conteneur d'injection de dépendance
      va voir que ce Port est implémenté par `AccountService` et injectera donc une instance qui-va-bien ;
    * Dans le constructeur, lier l'étape Given du scénario avec l'action d'ouverture d'un compte :
    ```java
       public AccountOpeningSteps(final AccountOpeningService accountOpeningService) {
           Given("{} has a newly opened bank account held in {}", (String id, String currency) -> accountOpeningService.openAccount(Account.Id.of(id), CurrencyUnit.of(currency)));
       }
    ```

On pourrait partir bille en tête et faire la même chose pour les 2 autres étapes. Mais on s'aperçoit qu'on va
probablement souvent écrire `Account.Id.of(id)` et aussi souvent créer des instances de `Money`. L'idéal serait de
pouvoir écrire quelque chose comme :
```java
Given("{accountId} has a newly opened bank account held in {currency}", (Account.Id id, CurrencyUnit currency) -> accountOpeningService.openAccount(id, currency));
```

Et avec la magie des références de méthodes, ça pourrait même se simplifier en :
```java
Given("{accountId} has a newly opened bank account held in {currency}", accountOpeningService::openAccount);
```

C'est exactement ce qu'on va faire avant d'aller plus loin grâce à la création de types de paramètres :

1. Créer une classe de test `fr.esiha.katas.bank.account.driving.cucumber.AccountSteps` implémentant `io.cucumber.java8.En` :
    * Dans le constructeur, déclarer le `ParameterType` nommé `accountId` :
        ```java
        ParameterType("accountId", "[A-Z]\\w+", Account.Id::of);
        ```
2. En faire de même pour les types de paramètres `currency` et `money` (`Money::parse` est votre amie) dans une classe
   `fr.esiha.katas.bank.account.driving.cucumber.JodaMoneySteps`.
3. Utiliser les paramètres `accountId` et `currency` dans l'étape `Given` préalablement décrite.

Maintenant, on peut s'atteler à la création des deux étapes manquantes pour jouer le Scénario au complet :
1. Créer une classe de test `fr.esiha.katas.bank.account.driving.cucumber.AccountDepositServiceSteps` déclarant l'étape
   `When` du scénario
2. Créer une classe de test `fr.esiha.katas.bank.account.driving.cucumber.AccountRespositorySteps` et exploitant
   `AccountRepository` pour implémenter l'étape `Then` du scénario (AssertJ est votre ami).

Ajoutons maintenant un autre scénario à la fonctionnalité « Deposit » :
1. Définir et implémenter le scénario suivant :
    ```gherkin
    Scenario: Karen deposits money on her account several times
      Given Karen has a newly opened bank account held in EUR
      And Karen has deposited EUR 350 on her account
      When Karen deposits EUR 500 on her account
      Then Karen's account has a balance of EUR 850
    ```
2. Pour éviter de répéter l'étape `Given Karen has a newly opened bank account held in EUR`, la déplacer en
   `Background:` avant le premier scénario. Pensez à remplacer le `And` du second scénario par un `Given` pour que le
   scénario reste lisible.

### Pour la fonctionnalité « Withdrawal »

Pour cette fonctionnalité, vous vous apercevrez que l'un des scénarios n'est pas géré par le code métier. Je vous invite
à utiliser un peu de Double-Loop TDD pour l'implémenter une fois que vous aurez le scénario qui échoue. Si vous avez
configuré la classe `DomainTest` pour exclure les scénarios libellés `@unimplemented`, vous pourrez ignorer le scénario
qui échoue le temps de l'implémentation.

1. Créer le fichier `src/test/resources/features/withdrawal.feature` avec le contenu :
    ```gherkin
    Feature: Withdraw money from a bank account.

       In order to retrieve their savings, bank account clients can withdraw money from their bank account.
    ```
2. Définir et implémenter le scénario :
    ```gherkin
    Scenario: Roger withdraws part of his savings
       Given Roger has a bank account with a balance of EUR 1500
       When Roger withdraws EUR 250 from his account
       Then Roger's account has a balance of EUR 1250
    ```
2. Définir et implémenter le scénario :
    ```gherkin
    Scenario: Emma withdraws all of her savings
       Given Emma has a bank account with a balance of EUR 1500
       When Emma withdraws EUR 1500 from her account
       Then Emma's account has a balance of EUR 0
    ```
3. Définir et implémenter le scénario :
    ```gherkin
    Scenario: Roger tries to withdraw more than his savings
       Given Roger has a bank account with a balance of EUR 1200
       When Roger withdraws EUR 2000 from his account
       Then the withdrawal is refused
       And Roger's account has a balance of EUR 1200
    ```

**Indications utiles**
* Vous pouvez utiliser la syntaxe `"foo/bar"` pour créer une alternative entre "foo" et "bar"
* Vous pouvez injecter `AccountDepositService` dans `AccountOpeningSteps` pour implémenter les `Given`.
* Vous aurez des déclarations `When` et `Then` dans `AccountWithdrawalSteps`, c'est normal.

### Pour la fonctionnalité « History »

Cette fonctionnalité est l'occasion de découvrir comment alléger l'écriture et la lecture d'étapes successives semblables.

1. Créer le fichier `src/test/resources/features/history.feature` avec le contenu ci-dessous et implémenter les étapes
   correspondantes :
    ```gherkin
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
    ```

**Indications utiles**
* Il est possible de décrire un `ParameterType` qui permet d'obtenir un `Predicate<Operation>` pour simplifier l'écriture.
* Exposer l'expression régulière correspondant à `JodaMoney` en constante de `JodaMoneySteps` évite de la dupliquer.
* `List::indexOf` est votre amie pour faire correspondre "first", "second" et "third" à un entier.
