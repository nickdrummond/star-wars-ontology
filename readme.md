home |
[browse](https://www.star-wars-ontology.co.uk/) |
[docs](docs/)

[benefits](docs/benefits.md) |
[modules](docs/modularisation.md) |
[events](docs/events.md) |
[modelling principles](docs/modelling-principles.md) |
[test questions](docs/test-questions.md) |
[performance](docs/performance.md) |
[tools](docs/tools.md)

# Project Convor - Star Wars Ontology

[Convor](https://www.star-wars-ontology.co.uk/classes/-1326117872/), as in "OWL in Star Wars".

A hand-built [OWL ontology](docs/benefits.md) focused on events and covering characters, places and things in the films and TV series.

[Browse the ontology](https://www.star-wars-ontology.co.uk/)
This allows you to navigate asserted content, search and query the ontologies.

[![Investigating_the_World_Between_Worlds](docs/images/ontology-browser.png)](https://www.star-wars-ontology.co.uk/individuals/1184063215/)

## Metrics

| Content                                                                                                                  | Count |
|--------------------------------------------------------------------------------------------------------------------------|-------|
| [Events](https://www.star-wars-ontology.co.uk/dlquery/?expression=Event&syntax=man)                                   | 915   |
| [Characters](https://www.star-wars-ontology.co.uk/dlquery/?expression=Being+or+Droid&syntax=man)                      | 614   |
| [Species](https://www.star-wars-ontology.co.uk/dlquery/?expression=Living_thing&syntax=man&query=descendants)        | 200   |
| [Planets and Moons](https://www.star-wars-ontology.co.uk/dlquery/?expression=Planet+or+Moon&syntax=man)               | 147   |
| [Built locations](https://www.star-wars-ontology.co.uk/dlquery/?expression=Built_Location+and+not+Vehicle&syntax=man) | 165   |
| [Organisations or units](https://www.star-wars-ontology.co.uk/dlquery/?expression=Organisation&syntax=man)            | 134   |
| [Named vehicles](https://www.star-wars-ontology.co.uk/dlquery/?expression=Vehicle&syntax=man)                         | 127   |

| Structure             | Count  |
|-----------------------|--------|
| Axiom                 | 31,636 |
| Logical axioms        | 15,387 |
| Declaration axioms    | 3,365  |
| Individuals           | 2,261  |
| Classes               | 999    |
| Object properties     | 95     |
| Data properties       | 4      |

Above is a snapshot - see [ontology metrics](https://www.star-wars-ontology.co.uk/ontologies/) for current values.

## Status

* Active development
* Open modelling questions - see [issues on github](https://github.com/nickdrummond/star-wars-ontology/issues)
* Tests and queries framework
* Modularisation of events by film/series

## Scope

| Content                     | Status                                                                                   |
|-----------------------------|------------------------------------------------------------------------------------------|
| Skywalker Saga episodes 1-9 | first pass                                                                               |
| Solo                        | first pass                                                                               |
| Rogue One                   | first pass                                                                               |
| The Mandalorian             | [2 of 3 series first pass](https://github.com/nickdrummond/star-wars-ontology/issues/45) |
| Resistance                  | first pass                                                                               |
| Rebels                      | first pass                                                                               |  
| Clone Wars                  | [4 of 7 series first pass](https://github.com/nickdrummond/star-wars-ontology/issues/11) |
| The Bad Batch               | [1 of 3 series first pass](https://github.com/nickdrummond/star-wars-ontology/issues/44) |
| The Book of Boba Fett       | first pass                                                                               |
| Kenobi                      | first pass                                                                               |
| Andor                       | [started](https://github.com/nickdrummond/star-wars-ontology/issues/39)                  |
| Tales of the Jedi           | to do                                                                                    |
| Ahsoka                      | to do                                                                                    |
| Tales of the Empire         | to do                                                                                    |
| The Acolyte                 | to do                                                                                    |

## Contents

* [docs](docs/) - notes on specific modelling issues, discussion of the benefits of using OWL, and working docs
* [ontologies](ontologies/) - Ontologies in ttl (Turtle) OWL format. Start with [all.owl.ttl](ontologies/all.owl.ttl)
* [src/test](https://github.com/nickdrummond/star-wars-ontology/tree/master/src/test) - A set of Java tests against the ontology for quality checking etc
* [src/main](https://github.com/nickdrummond/star-wars-ontology/tree/master/src/main) - Transforms and other code
* [sparql](sparql/) - A set of Sparql queries to summarise or infer things about our ontology

## Usage

### Browse

[https://www.star-wars-ontology.co.uk/](https://www.star-wars-ontology.co.uk/)

Please be patient with queries - this is hosted on cheap(ish) hobby-level hosting which I pay for
myself and receive no funding.

Browser contains [all.owl.ttl](ontologies/all.owl.ttl) and it's imports closure
(not [behind-the-scenes.owl.ttl](ontologies/behind-the-scenes.owl.ttl))

For efficiency, the query page only includes [event.owl.ttl](ontologies/events.owl.ttl) and
its imports - see [docs/performance.md](docs/performance.md)

[![Cloud view of individuals by usage](docs/images/cloud.png)](https://www.star-wars-ontology.co.uk/clouds/individuals)


#### Local browsing with Docker and ontology browser

Clone this repository.
From the root of this project, build a docker image and run it with:
```
docker build -t nickdrummond/star-wars-ontology .
docker run -d -p 8080:8080 nickdrummond/star-wars-ontology
```
Then browse to [http://localhost:8080/](http://localhost:8080/) to see the ontology browser.

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
