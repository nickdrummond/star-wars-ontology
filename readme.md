[docs](docs/) |
[benefits](docs/benefits.md) |
[modules](docs/modularisation.md) |
[events](docs/events.md) |
[modelling principles](docs/modelling-principles.md) |
[test questions](docs/test-questions.md) |
[performance](docs/performance.md) |
[tools](docs/tools.md)

# Project Convor - Star Wars Ontology

[Convor](http://star-wars-ontology.herokuapp.com/classes/-1326117872/), as in "OWL in Star Wars".

A hand-built [OWL ontology](docs/benefits.md) focused on events and covering characters, places and things in the films and TV series.

[Browse the ontology](https://star-wars-ontology.herokuapp.com/) on heroku
(first page may take a second to load as its using free tier hosting).
This currently only displays asserted content.

[![Star Wars Ontology (cloud view)](docs/cloud.png)](docs/instances-usage-cloud.pdf)

## Metrics

| Content                                                                                                                  | Count |
|--------------------------------------------------------------------------------------------------------------------------|-------|
| [Events](http://star-wars-ontology.herokuapp.com/dlquery/?expression=Event&syntax=man)                                   | 520   |
| [Characters](http://star-wars-ontology.herokuapp.com/dlquery/?expression=Being+or+Droid&syntax=man)                      | 431   |
| [Species](https://star-wars-ontology.herokuapp.com/dlquery/?expression=Living_thing&syntax=man&query=descendants)        | 168   |
| [Planets and Moons](http://star-wars-ontology.herokuapp.com/dlquery/?expression=Planet+or+Moon&syntax=man)               | 121   |
| [Built locations](http://star-wars-ontology.herokuapp.com/dlquery/?expression=Built_Location+and+not+Vehicle&syntax=man) | 101   |
| [Organisations or units](http://star-wars-ontology.herokuapp.com/dlquery/?expression=Organisation&syntax=man)            | 147   |
| [Named vehicles](http://star-wars-ontology.herokuapp.com/dlquery/?expression=Vehicle&syntax=man)                         | 89    |

| Structure             | Count  |
|-----------------------|--------|
| Axiom                 | 15,101 |
| Logical axioms        | 10,686 |
| Declaration axioms    | 2,420  |
| Individuals           | 1,496  |
| Classes               | 806    |
| Object properties     | 112    |
| Data properties       | 6      |

Above is a snapshot - see [ontology metrics](http://star-wars-ontology.herokuapp.com/ontologies/) for current values.

## Status

* In progress - see scope below
* Open modelling questions - see [issues on github](https://github.com/nickdrummond/star-wars-ontology/issues)
* Tests and queries framework
* Modularisation of events by film/series

## Scope

* Episodes 1-9 - first pass
* Solo and Rogue One - first pass
* The Mandalorian - first pass
* Resistance - first pass
* Rebels - first pass
* Clone Wars - in progress
* The Bad Batch - first pass
* The Book of Boba Fett - to do

## Contents

* [docs](docs/) - notes on specific modelling issues, discussion of the benefits of using OWL, and working docs
* [ontologies](ontologies/) - Ontologies in ttl (Turtle) OWL format. Start with [all.owl.ttl](ontologies/all.owl.ttl)
* [src/test](src/test/) - A set of Java tests against the ontology for quality checking etc
* [sparql](sparql/) - A set of Sparql queries to summarise or infer things about our ontology

## Usage

### Browse

[https://star-wars-ontology.herokuapp.com/](https://star-wars-ontology.herokuapp.com/)

Please be patient - this is on a free-tier heroku stack for demo purposes only.

Browser contains [all.owl.ttl](ontologies/all.owl.ttl) and it's imports closure
(not [behind-the-scenes.owl.ttl](ontologies/behind-the-scenes.owl.ttl))

For efficiency, the query page only includes [event.owl.ttl](ontologies/events.owl.ttl) and
its imports - see [docs/performance.md](docs/performance.md)

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
