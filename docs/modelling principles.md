# General modelling principles

[back to index](index.md)

## References
- all individuals should have an appropriate "seeAlso" reference to Wookipedia
- all non-obvious Classes should also reference Wookipedia

## Tense
- properties should be in past tense "A long time ago..."

## Properties
- should be used more than once otherwise they need reviewing
- should be asserted in one direction, inverses being inferred - eg "after"

## Event focussed
- events within events using subX during X - not other way round
- assertions about involvement attached to the Event, not the person (unless the event is not worth naming)

## People
 - property assertions between people on the one it affects most (eg the one who died in a fight) unless Event called for
 - should all have at least one Role (whether or not in an organisation)

## from (where born/grew up)
 - Asserting at instance level where people are from
 - Use homeworld on Planets/Moons for a weaker reference to species (also works with modularisation as species are Classes).

## Force ghosts/connections

Force ghosts and connections are manifestations of the Force, not the individual.

    participant some (Force_spirit and (connectedTo value Obi-Wan_Kenobi))
    participant some (The_Force and (connectedTo value Leia_Organa))

## Information

Information is treated as an object in the Universe, can have a subject and can be traded or found.

    Information and (about value Luke_Skywalker)

We might be interested in the form of the information

    Plans and (about value Death_Star)    

Events like `Meeting` are not an object but may also have a subject

    Meeting and (about value First_Order)
