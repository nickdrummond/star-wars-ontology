# Star Wars Ontology

## Scope

The ontology covers major characters, events and places in the canon films and TV series.
* Episodes 1-9
* Solo and Rogue One
* The Mandalorian
* Clone Wars, Rebels, Resistance, The Bad Batch

## Contents

* [star-wars.owl.ttl](ontologies/star-wars.owl.ttl) - Ontology of Star Wars Universe characters, events and places from the major canon film and TV series
* [src/test](src/test/) - A set of Java tests against the ontology for quality checking etc
* [sparql](sparql/) - A set of Sparql queries to summarise or infer things about our ontology
* [docs](docs/) - notes on specific modelling issues, discussion of the benefits of using OWL, and working docs

## Status

* In progress
* Basic modelling of all live action content completed
* Many open modelling questions
* Tests and queries still experimental
* Initial modularisation experiments

## Metrics

|Type |Count |
--- | ---
|Axiom |5539
|Logical axioms |3695
|Declaration axioms	|1019
|Classes	|342
|Object properties	|79
|Data properties	|4
|Individuals	|593
|Annotation Properties	|5

---

## Todo

* Can we easily query/infer a timeline for a given character?
* update tests to work with imports

* Series
    * Rebels - 4 series eps(15, 22, 22, 16)
    * The Clone Wars series 7 series eps(22, 22, 22, 22, 20, 13, 12)
    * Bad Batch - 1 series eps(incomplete)

* Changes to base cannot be saved if other ontologies loaded - https://github.com/protegeproject/protege/pull/1025

* Review property "of" - is it even defined properly?

* "visited" only used once - is it useful?

* "healed" also only used once

* "surrenderOf" - anywhere else to use this?

* is "on" really helping? It's just a nice bit of syntactic sugar

* Can we have ABY/BBY instead of int as custom datatype?