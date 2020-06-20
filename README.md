Ceci est une implémentation du Kata de Comptes Bancaires. Il sert de point de départ pour un ensemble d'exercices
d'introduction à Cucumber, décrits dans [EXERCISE.md](EXERCISE.md).

Pour compiler et exécuter tous les tests, il est nécessaire d'utiliser un JDK 11 et une version récente de Maven. Ensuite,
il n'y a qu'à lancer cette commande dans le dossier courant :

```shell script
mvn test
```

Voici une vue d'ensemble du Modèle du Domaine, générée partir du diagramme de classes simplifié [domain model.puml](domain%20model.puml) :

![](domain%20model.png)

#### Choix faits pour l'implémentation

1. Application des principes du TDD.
1. Utilisation de l'Architecture Hexagonale pour séparer le code métier des briques technologiques qui viennent l'intégrer
   au reste du monde. Cependant, Joda Money est autorisé dans le domaine parce que : a. c'est tout petit ; b. sa philosophie
   de développement est en phase avec celle de cet exercice.
1. Application du Model-Driven Design pour construire un Modèle de Domaine adapté au besoin.
1. Application des Tactical Patterns et du Supple Design, tels que décrits dans le Domain-Driven Design, pour implémenter
   le Modèle de Domaine élaboré.
1. Application du principe Command-Query Separation.
1. Utilisation de Cucumber pour implémenter les tests du Domaine <-- Hey, vous filez un coup de main pour finir le Kata ;)
