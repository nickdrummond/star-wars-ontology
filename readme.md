# Star Wars Ontology

## Scope

The ontology covers major characters, events and places in the canon films and TV series.
* Episodes 1-9
* Solo and Rogue One
* The Mandalorian
* Clone Wars, Rebels, Resistance, The Bad Batch

## Contents

* [star-wars.owl.ttl](star-wars.owl.ttl) - Ontology of Star Wars Universe characters, events and places from the major canon film and TV series
* [src/test](src/test/) - A set of Java tests against the ontology for quality checking etc
* [sparql](sparql/) - A set of Sparql queries to summarise or infer things about our ontology
* [docs](docs/) - notes on specific modelling issues, discussion of the benefits of using OWL, and working docs

## Status

* In progress
* Basic modelling of films completed
* Many open modelling questions
* Tests and queries still experimental
* No modularisation currently

---

## Todo

* Series
    * Clone Wars series
    * Rebels
    * Resistance
    * Bad Batch
* Review property "of" - is it even defined properly?
* "visited" only used once - is it useful?
* "healed" also only used once
* "surrenderOf" - anywhere else to use this?
* is "on" really helping? It's just a nice bit of syntactic sugar

* Revise classification of living things - sentience makes it difficult to classify by type (eg Reptile)
  
* Homeworld for each species - might add a lot of places that aren't interesting in any other way

* We can add images in seeAlso - jpg extension on anyURI. But, cannot manage copyright and hot loading
Unfortunately, probably no pattern to generate automatically
eg from Star Wars databank https://lumiere-a.akamaihd.net/v1/images/databank_zygerria_01_169_5cdad909.jpeg

* Can we have ABY/BBY instead of int as custom datatype?