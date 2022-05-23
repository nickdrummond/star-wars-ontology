[docs](index.md) |
[benefits](benefits.md) |
[modules](modularisation.md) |
[events](events.md) |
[modelling principles](modelling-principles.md) |
[test questions](test-questions.md) |
performance |
[tools](tools.md)

# Performance

For the sake of performance, some peripheral assertions have been moved into their own ontologies. These are still imported by all.owl.ttl

* species.owl.ttl - traits, anatomy etc
* manufacturers.owl.ttl - creation of ships/droids etc

Although ```all.owl.ttl``` works with Pellet, it is significantly more responsive 
reasoning and querying with ```events.owl.ttl```

## Reasoners

Initial testing on `all.owl` using `participatedIn some Murder`:

|Reasoner |Works? | Notes
--- | --- | --- 
|Pellet                 |Yes       | Fast. See notes on datatype restrs *
|Pellet (Incremental)   |Yes-ish   | DL Query not working? see above for other issues
|HermiT                 |Yes       | Slow. 66-72s
|JFact                  |?         | Need to build from source?
|ELK                    |Partially | fast but v. incomplete - finds Ochi, Ziro but ignores all results from Events. Does not support DataPropertyAssertions, some DataHasValue, some Unions. Some tabs do not work

Pellet note - min/max Exclusive causing error in unknown circumstances:
  

    InternalReasonerException: Adding type to a pruned node "anon(222)" http://www.w3.org/2001/XMLSchema#int


## Speed Tests
We have some basic metrics being checked - see [ConsistencyTest](../src/test/java/com/nickd/sw/ConsistencyTest.java)

There could be a lot of other queries testing

## Expensive modelling (Pellet)

### Cardinality

In some specific cases, cardinality on oneOfs cause a large hit.
They are normally used as a syntax shortcut and should be avoided.
See [Issue #3](https://github.com/nickdrummond/star-wars-ontology/issues/3)

### Characterising species

36-38s full classification in Protege of all.owl.ttl

* Moving hasPart usage (heavy use of cardinality) to another ontology not directly imported
= 26-27s classification for all.owl.ttl
* Moving hasTrait usage only saves a second, but fits better in the new ontology
* Moving sentience usage drops classification to 15s
* Moving movesWith usage drops it all the way to 12s

Assertions on individuals have been retained in star-wars.owl.ttl

### Characterising vehicles/roles

Leaving movesWith usage for 6 type of vehicles and troopers in base.owl.ttl
adds 2/3s to classification

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


## Different individuals

If we add an allDifferent (and remove Darths/Rens) what else can we assert / query?

For a start, it grinds the reasoner to a halt
