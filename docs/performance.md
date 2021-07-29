# Performance

## Speed Tests
We have some basic metrics being checked - see [ConsistencyTest](../src/test/java/com/nickd/sw/ConsistencyTest.java)

Classification time =~ 0.5s

Murder query time =~ 10-13s

There could be a lot of other queries testing

## Expensive modelling

### allDifferent

Initial trial of allDifferent on eg Humans increased classification times significantly
TODO try again for stats

### Property Domain/Range

Removing domain and range on all object properties dropped the query time from 28s to 10s

However, we do lose helpful consistency checking.
eg participants in Events must not include Roles - leaving this range on participant is not too bad.
Whereas, adding a range back on "during" automatically adds a few seconds on.

### Cardinality
Cannot add participant min 14000 to the battle of Exegol - it breaks the reasoner