# Documentation

[back to readme](../readme.md)

## Content

### Characters, Places, Species, Things
Sentient and non-sentient beings, planets, cities, ships, weapons, etc

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
