# Star Wars Ontology

## Scope

The ontology covers major characters, events and places in the canon films and TV series.
* Episodes 1-9
* Solo and Rogue One
* The Mandalorian
* Clone Wars, Rebels, Resistance, The Bad Batch

## Contents

* [ontologies](ontologies/) - Ontologies of Star Wars Universe characters, events and places from the major canon film and TV series. Start with [all.owl.ttl](ontologies/all.owl.ttl)
* [src/test](src/test/) - A set of Java tests against the ontology for quality checking etc
* [sparql](sparql/) - A set of Sparql queries to summarise or infer things about our ontology
* [docs](docs/) - notes on specific modelling issues, discussion of the benefits of using OWL, and working docs

## Status

* In progress
* Basic modelling of all live action content completed
* Many open modelling questions
* Tests and queries still experimental
* Modularisation of events by film/series

## Metrics

|Type |Count |
--- | ---
|Events                 |257
|Characters             |266
|Species                |129
|Planets and Moons      |82
|Built locations        |48
|Organisations or units |56
|Named vehicles         |50

|Structure |Count |
--- | ---
|Axiom                  |7544
|Logical axioms         |5146
|Declaration axioms	    |1301
|Classes	            |421
|Object properties	    |85
|Data properties    	|4
|Individuals	        |790
|Annotation Properties	|5

---

## Declaration

This work is not sanctioned or otherwise connected to Lucasfilm, Disney or it's affiliates.
It is an unpaid experiment in representing an interesting, complex domain of storytelling.
Copyright of all names and references remain with their respective owners or creators.
There is no assertion of correctness or completeness by myself - content has been
created using publically available, free content created by fans (ie Wookipedia).

That said, I would love to talk to Leland Chee about the possibilities of using RDF/OWL
as a way of helping the continuity and story group within the Star Wars teams.

## Todo

* Spend some time in the Individual Hierarchy Tab (Protege)
  * Requires the reasoner for inv property tree
  * Remove `Transitive(location)` and navigate through this hierarchy - nice

* Can we easily query/infer a timeline for a given character?
  * we can easily [query for the events](docs/events.md) using simple DL query, but how do we put them in some order?
  * If we want to get the events and subevents for a character, we either query this or we have
a subchain (the subchain is great as it brings the named events in to the inferred props but what is the cost?)

  
    included o participant -> participant


* Check all events have dates and are not orphaned - see Sparql test

* Series
    * Rebels - series 2(22) 3(22), 4(16)
    * The Clone Wars series 7 series eps(22, 22, 22, 22, 20, 13, 12)
    * Bad Batch - 1 series eps(incomplete)

* Review property "of" - is it even defined properly?

* "visited" only used once - is it useful?

* "healed" also only used once

* "surrenderOf" - anywhere else to use this?

* Can we have ABY/BBY instead of int as custom datatype?


* Changes to base cannot be saved if other ontologies loaded - https://github.com/protegeproject/protege/pull/1025
