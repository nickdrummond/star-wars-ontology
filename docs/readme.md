[home](../) |
[browse](https://star-wars-ontology.up.railway.app/) |
docs

[benefits](benefits.md) |
[modules](modularisation.md) |
[events](events.md) |
[modelling principles](modelling-principles.md) |
[test questions](test-questions.md) |
[performance](performance.md) |
[tools](tools.md)

# Project Convor Documentation

## Why OWL? What is an Ontology?

For a brief idea of what an OWL ontology is and discussion of the benefits of using OWL for modelling see
[benefits.md](benefits.md).

## Content

The Star Wars Ontology describes things and events in the Universe and
the properties that join them together.

### Characters, Places, Species, Objects
[Sentient](https://star-wars-ontology.up.railway.app/classes/-2022683789/) 
and [non-sentient](https://star-wars-ontology.up.railway.app/classes/1920330355/)
[beings](https://star-wars-ontology.up.railway.app/classes/1008857208/),
[planets](https://star-wars-ontology.up.railway.app/classes/-957104751/),
[cities](https://star-wars-ontology.up.railway.app/classes/946892132/),
[ships](https://star-wars-ontology.up.railway.app/classes/947367477/),
[weapons](https://star-wars-ontology.up.railway.app/classes/-763163115/), etc

Although the ontology structure is still changing, we would like to have some consistency of style - see [modelling principles](modelling%20principles.md) for more information

### Events
This is the bulk of the storytelling aspect of the ontology which pulls everything else together

See [discussion of events](events.md)

## Modularisation

The ontology is split into several "layers" and each set of events from a given film or series is
maintained within its own module.

See [discussion of modularisation](modularisation.md)

## Performance

Users of ontologies will probably want to make use of a reasoner
to infer many of the relations when querying the model. Care must be taken to
ensure that the model balances scale and expresivity against the time for classification
or queries to take place.

Some of these experiments are documented in the [performance docs](performance.md)

## Test Questions

What should we be able to query for? What types of things are interesting about this
ontology?

Here are [example test questions](test%20questions.md)

## Tooling

The benefit of using w3c standard languages for modelling is the tool support.
We have used Stanford University's [Protege](https://protege.stanford.edu) to build the ontology
but there are plenty of other [tools](tools.md) that may help browse or query the ontology.
