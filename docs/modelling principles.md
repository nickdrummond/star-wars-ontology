# General modelling principles

[back to index](index.md)

## References

- should use `seeAlso` to reference all Individuals in Wookipedia
- should reference all non-obvious Classes in Wookipedia

## Properties

- should name properties in past tense "A long time ago..."
- should be used more than once otherwise they need reviewing
- should be asserted in one direction, inverses being inferred - eg "after"

## Disjoints/differentFrom

- should have top level disjoints to assist clean modelling and aid property use consistency checking
- should not use differentFrom because of its impact on [performance](performance.md)

## Negation

- should use negation sparingly as it is a strong assertion at risk of being wrong - eg "no survivors"


    not (survivedBy some (Tusken_Raider and (from value Tusken_Raider_camp)))

## Event focussed

* should provide a `year` for all named events, if known
  * negative numbers are BBY, positive are ABY  
- may attach involvement to an event to the `Actor`, if there is no natural named Event to use


    Ochi -> participatedIn some (Murder 
    and (deathOf some (parentOf value Rey)))

###  Timeline

- should place events in the timeline using `eventB after eventA`, if known
- should use `someTimeAfter` when larger time gaps between events (eg between series)
- should use `during` between named events - not its inverse
- should use `included` in subclassOf restrictions on event if its not worth naming


    Escape_from_the_Garbage_Compactor -> included some(Attack 
    and (participant some Dianoga)
    and (survivedBy value Luke_Skywalker))

## Beings

- primary classification is Species
- should assert relations between beings on the one it affects most - eg `Luke trainedBy Obi-wan` 
unless an `Event` is called for
- should all have at least one `Role` (whether or not in an organisation)


## from (where born/grew up)

- should use `from` at instance level where people originate
- should use `homeworld` on Planets/Moons for a weaker reference to species (also works with modularisation as species are Classes).
- should use `livedIn` for any other location someone spent their time 

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
