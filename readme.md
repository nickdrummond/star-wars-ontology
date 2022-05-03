# Project Convor - Star Wars Ontology

[Convor](http://star-wars-ontology.herokuapp.com/classes/1070940699/), as in "OWL in Star Wars".

A hand-built [OWL](https://www.w3.org/OWL/) ontology covering characters, events and places in the films and TV series.

[Browse the ontology](https://star-wars-ontology.herokuapp.com/) on heroku
(first page may take a second to load as its using free tier hosting).
This currently only displays asserted content.

[![Star Wars Ontology (cloud view)](docs/cloud.png)](docs/instances-usage-cloud.pdf)


## Scope

* Episodes 1-9 - first pass
* Solo and Rogue One - first pass
* The Mandalorian - first pass
* Resistance - first pass
* Rebels - first pass
* Clone Wars - in progress
* The Bad Batch - first pass
* The Book of Boba Fett - to do

## Status

* In progress - see scope
* Open modelling questions
* Tests and queries framework
* Modularisation of events by film/series

## Contents

* [docs](docs/index.md) - notes on specific modelling issues, discussion of the benefits of using OWL, and working docs
* [ontologies](ontologies/) - Ontologies in ttl (Turtle) OWL format. Start with [all.owl.ttl](ontologies/all.owl.ttl)
* [src/test](src/test/) - A set of Java tests against the ontology for quality checking etc
* [sparql](sparql/) - A set of Sparql queries to summarise or infer things about our ontology

## Metrics

| Content                | Count |
|------------------------|-------|
| Events                 | 509   |
| Characters             | 424   |
| Species                | 167   |
| Planets and Moons      | 121   |
| Built locations        | 92    |
| Organisations or units | 143   |
| Named vehicles         | 87    |

| Structure             | Count  |
|-----------------------|--------|
| Axiom                 | 14,589 |
| Logical axioms        | 10,310 |
| Declaration axioms    | 2,349  |
| Classes               | 786    |
| Object properties     | 112    |
| Data properties       | 6      |
| Individuals           | 1,445  |
| Annotation Properties | 5      |

## Usage

### Browse
* https://star-wars-ontology.herokuapp.com/

### Edit/reason
* Open [all.owl.ttl](ontologies/all.owl.ttl) with open-source OWL editor, [Protege](https://protege.stanford.edu/) or
  another semantic web editor/browser
* Set the default reasoner to classify/reason with as `Pellet` on events.owl
* See [tools](docs/tools.md) for other tool related discussion

---

## Declaration

This work is not sanctioned or otherwise connected to Lucasfilm, Disney or it's affiliates. It is an unpaid experiment
in representing an interesting, complex domain of storytelling.

All names and references are Trademark and/or copyright of Disney and affiliates or their respective owners or creators.

There is no assertion of correctness or completeness by myself - content has been created referencing publicly
available, free content created by fans (ie Wookipedia).

That said, I would love to talk to Leland Chee about the possibilities of using RDF/OWL as a way of helping the
continuity and story group within the Star Wars teams.
