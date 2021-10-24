# Performance

[back to index](index.md)


For the sake of performance, some assertions have been put in
[test.owl.ttl](../ontologies/test.owl.ttl) so that inconsistencies can be
caught during the engineering of these ontologies but not
at the expense of classification times.

## Speed Tests
We have some basic metrics being checked - see [ConsistencyTest](../src/test/java/com/nickd/sw/ConsistencyTest.java)

Classification time =~ 0.5s

Murder query time =~ 10-13s

There could be a lot of other queries testing

## Expensive modelling

### allDifferent

allDifferent between individuals increased classification times significantly.

Murder query goes from 10s to 112s

### Property Domain/Range

Adding domain and range on all object properties increased the query time from 10s to 28s.

However, we do lose helpful consistency checking (as we have top level disjoints).
eg `participants` in `Events` are `Actors`, not `Roles` - leaving this range on `participant` is not too bad.
Whereas, adding a range to `during` automatically adds a few seconds on.

Domains/ranges have been added to the top level `test` ontology that is used purely
as a QA mechanism and does not need to be used at query time.

### Cardinality
Cannot add participant min 14000 to the battle of Exegol - it breaks the reasoner!