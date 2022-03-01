# Performance

[back to index](index.md)


For the sake of performance, some assertions have been put in
[test.owl.ttl](../ontologies/test.owl.ttl) so that inconsistencies can be
caught during the engineering of these ontologies but not
at the expense of classification times.

## Reasoners

Initial testing on `all.owl` using `participatedIn some Murder`:

|Reasoner |Works? | Notes
--- | --- | --- 
|Pellet                 |Yes       | Fast. Be notes on datatype restrs *
|Pellet (Incremental)   |Yes-ish   | DL Query not working? see above for other issues
|HermiT                 |Yes       | Slow. 66-72s
|JFact                  |?         | Need to build from source?
|ELK                    |Partially | fast but v. incomplete - finds Ochi, Ziro but ignores all results from Events. Does not support DataPropertyAssertions, some DataHasValue, some Unions. Some tabs do not work

* Pellet note - min/max Exclusive causing error in unknown circumstances:
  

    InternalReasonerException: Adding type to a pruned node "anon(222)" http://www.w3.org/2001/XMLSchema#int


## Speed Tests
We have some basic metrics being checked - see [ConsistencyTest](../src/test/java/com/nickd/sw/ConsistencyTest.java)

There could be a lot of other queries testing

## Expensive modelling (Pellet)

### ```visited``` as a property chain
Nice to have this to show where characters have been, but it can be used at query time instead as its slow:

    participatedIn o location -> visited

### Property Domain/Range
Adding property domain/ranges base.owl
Classification time of rebels.owl reduced from 23.5s to 18s


## Expensive modelling (HermiT) - to revise using Pellet

### allDifferent

There seems to be no significant overhead using Pellet (unlike HermiT).

If we do local allDifferent (each species/planets/etc) then we can get more cardinality queries, eg:

    Event and (participant min 2 (hadRole some Mandalorian))

### Property Domain/Range

Adding domain and range on all object properties increased the query time from 10s to 28s.

However, we do lose helpful consistency checking (as we have top level disjoints).
eg `participants` in `Events` are `Actors`, not `Roles` - leaving this range on `participant` is not too bad.
Whereas, adding a range to `during` automatically adds a few seconds on.

Domains/ranges were added to a top level `test` ontology which has now been removed using pellet.

### Cardinality
Cannot add participant min 14000 to the battle of Exegol - it breaks the reasoner!