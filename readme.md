# Project Convor - Star Wars Ontology

Convor, as in "OWL in Star Wars".

An [OWL](https://www.w3.org/OWL/) ontology covering major characters, events and places in the canon films and TV series.

## Scope

* Episodes 1-9 - first pass
* Solo and Rogue One - first pass
* The Mandalorian - first pass
* Resistance - first pass
* Rebels - in progress
* Clone Wars - skeleton
* The Bad Batch - skeleton

## Status

* In progress - see [TO DO](docs/todo.md) list
* First pass modelling of content completed up to Rebels
* Many open modelling questions
* Tests and queries still experimental
* Modularisation of events by film/series

## Contents

* [docs](docs/index.md) - notes on specific modelling issues, discussion of the benefits of using OWL, and working docs
* [ontologies](ontologies/) - Ontologies in ttl (Turtle) OWL format. Start with [all.owl.ttl](ontologies/all.owl.ttl)
* [src/test](src/test/) - A set of Java tests against the ontology for quality checking etc
* [sparql](sparql/) - A set of Sparql queries to summarise or infer things about our ontology


## Metrics

|Content |Count |
--- | ---
|Events                 |410
|Characters             |309
|Species                |148
|Planets and Moons      |101
|Built locations        |69
|Organisations or units |81
|Named vehicles         |70

|Structure |Count |
--- | ---
|Axiom                  |11,565
|Logical axioms         |8,157
|Declaration axioms	    |1,890
|Classes	            |655
|Object properties	    |118
|Data properties    	|6
|Individuals	        |1,111
|Annotation Properties	|5


## Usage

* Open [all.owl.ttl](ontologies/all.owl.ttl) with open-source OWL editor, [Protege](https://protege.stanford.edu/) or another semantic
  web editor/browser
* Set the default reasoner to classify/reason with as `Pellet`
* See [tools](docs/tools.md) for other tool related discussion

---

## Declaration

This work is not sanctioned or otherwise connected to Lucasfilm, Disney or it's affiliates.
It is an unpaid experiment in representing an interesting, complex domain of storytelling.

All names and references are Trademark and/or copyright of Disney and affiliates or their respective owners or creators.

There is no assertion of correctness or completeness by myself - content has been
created referencing publicly available, free content created by fans (ie Wookipedia).

That said, I would love to talk to Leland Chee about the possibilities of using RDF/OWL
as a way of helping the continuity and story group within the Star Wars teams.
