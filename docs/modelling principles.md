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
- Named events should be placed in the timeline where possible using `eventB after eventA`
- Named events within other events using `subX during X` - not other way round
- Use `included` in subclass of event if its not worth naming
- assertions about involvement attached to the Event, not the person (unless the event is not worth naming)
- All named events should have a `year` if known

## People

- property assertions between people on the one it affects most - eg `Luke trainedBy Obi-wan` 
unless an Event is called for
- should all have at least one Role (whether or not in an organisation)


## from (where born/grew up)

- Use `from` at instance level where people originate
- Use `homeworld` on Planets/Moons for a weaker reference to species (also works with modularisation as species are Classes).
- Use `livedIn` for any other location someone spent their time 

## The Force

Force ghosts, bonds and connections are manifestations of the Force, not the individual.

    participant some (Force_spirit
        and (connectedTo value Obi-Wan_Kenobi))

    participant some (Force_Bond
        and (connectedTo value Ahsoka_Tano)
        and (connectedTo value Darth_Vader))
    
    participant some (The_Force
        and (connectedTo value Leia_Organa))

Force visions are also manifestations of the force, connected to the individual experiencing
them. The content of the vision is described using `about`

    participant some (Force_Vision
        and (connectedTo value Ezra_Bridger)
        and (about some (parentOf value Ezra_Bridger))
        and (about value White_Loth_Cat))

## Information

Information is treated as an object in the Universe, can have a subject and can be traded or found.

    Information and (about value Luke_Skywalker)

We might be interested in the form of the information

    stole some (Plans and (about value Death_Star))    

`Communication` events are not an object but may also have a subject

    Meeting and (about value First_Order)
