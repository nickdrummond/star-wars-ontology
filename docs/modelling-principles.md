[home](../) |
[browse](https://www.star-wars-ontology.co.uk/) |
[docs](readme.md)

[benefits](benefits.md) |
[modules](modularisation.md) |
[events](events.md) |
modelling principles |
[test questions](test-questions.md) |
[performance](performance.md) |
[tools](tools.md)

# Modelling principles

## References

- should use [`seeAlso`](https://www.star-wars-ontology.co.uk/annotationproperties/-1773693006/) to reference all Individuals in Wookipedia
- should reference all non-obvious Classes in Wookipedia

## Properties

- should name properties in past tense "A long time ago..."
- should be used more than once otherwise they need reviewing
- should be asserted in one direction, inverses being inferred

eg [`after`](https://www.star-wars-ontology.co.uk/objectproperties/1037526453/) shows usage but 
[`before`](https://www.star-wars-ontology.co.uk/objectproperties/-448094376/) has no assertions

## Normalisation

- all Classes should have a single primitive parent, creating a tree structure
- all Individuals should have only 1 named type
- defined classes may be created to produce a polyhierarchy based on refining characteristics (eg Biped = has 2 legs)

## Disjoints/differentFrom

- should have top level disjoints to assist clean modelling and aid property use consistency checking

## Negation

- should use negation sparingly as it is a strong assertion at risk of being wrong - eg "no survivors"

eg [Torture_of_Shmi](https://www.star-wars-ontology.co.uk/individuals/-631305730/)

    not (survivedBy some (Tusken and originallyFrom value Tusken_Raider_camp))

## Event focussed

* should include a location
* should include at least one participant
* should provide a [`year`](https://www.star-wars-ontology.co.uk/dataproperties/948496406/) for all named events, if known
  * negative numbers are BBY, positive are ABY
* may include sub-events with ```included```


###  Timeline

- should place events in the timeline using `eventB after eventA`, if known
- should use [`someTimeAfter`](https://www.star-wars-ontology.co.uk/objectproperties/2047817844/) when larger time gaps between events (eg between series)
- should use [`during`](https://www.star-wars-ontology.co.uk/objectproperties/-375708134/) between named events - not its inverse
- should use [`included`](https://www.star-wars-ontology.co.uk/objectproperties/1035051157/) in subclassOf restrictions on event if its not worth naming

eg [Escape_from_the_Garbage_Compactor](https://www.star-wars-ontology.co.uk/individuals/-1663775856/):

    included some(Attack 
    and (participant some Dianoga)
    and (survivedBy value Luke_Skywalker))

## Beings

- primary classification is species
- should assert relations between beings on the one it affects most - eg `Luke trainedBy Obi-wan` 
unless an [`Event`](https://www.star-wars-ontology.co.uk/classes/1012130387/) is called for
- should all have at least one [`hadRole`](https://www.star-wars-ontology.co.uk/objectproperties/1627826554/) (whether or not in an organisation)


## from (where born/grew up)

- should use [`originallyFrom`](https://www.star-wars-ontology.co.uk/objectproperties/-1044081727/) at instance level where people originate
- should use [`homeworldOf`](https://www.star-wars-ontology.co.uk/objectproperties/418614051/) on Planets/Moons for a weaker reference to species (also works with modularisation as species are Classes).
- should use [`livedIn`](https://www.star-wars-ontology.co.uk/objectproperties/1129084950/) for any other location someone spent their time 

## The Force

[`Force_spirits`](https://www.star-wars-ontology.co.uk/classes/1763189694/),
[`Force_bonds`](https://www.star-wars-ontology.co.uk/classes/-1223412816/)
and [`connections`](https://www.star-wars-ontology.co.uk/objectproperties/-1625702595/)
are manifestations of [`The Force`](https://www.star-wars-ontology.co.uk/classes/-1757453002/),
not the individual.

[Search_for_Luke](https://www.star-wars-ontology.co.uk/individuals/919917289/):

    participant some (Force_spirit
        and (connectedTo value Obi-Wan_Kenobi))

[Attack_on_Phoenix_Squadron](https://www.star-wars-ontology.co.uk/individuals/2038845993/):

    participant some (Force_Bond
        and (connectedTo value Ahsoka_Tano)
        and (connectedTo value Darth_Vader))
    
[Rey_vs_Kylo_DeathStar](https://www.star-wars-ontology.co.uk/individuals/963336634/):

    participant some (The_Force
        and (connectedTo value Leia_Organa))

[`Force visions`](https://www.star-wars-ontology.co.uk/classes/926769109/) are also manifestations of the force, connected to the individual experiencing
them. The content of the vision is described using [`about`](https://www.star-wars-ontology.co.uk/objectproperties/1037402982/)

eg [Evacuation_of_Garel](https://www.star-wars-ontology.co.uk/individuals/1081848188/)

    participant some (Force_Vision
        and (connectedTo value Ezra_Bridger)
        and (about some (parentOf value Ezra_Bridger))
        and (about value White_Loth_Cat))

## Information

[`Information`](https://www.star-wars-ontology.co.uk/classes/286293221/) is
treated as an object in the Universe, can have a subject and can be used in
[trade](https://www.star-wars-ontology.co.uk/classes/1542077658/) or
[`found`](https://www.star-wars-ontology.co.uk/objectproperties/1042413403/).

eg [Meeting_Cid ](https://www.star-wars-ontology.co.uk/individuals/1729557587/):

    Trading and (of some (Information and about value Fennec_Shand))

We might be interested in the form of the information

eg [Infiltrating_the_Imperial_Armoury ](https://www.star-wars-ontology.co.uk/individuals/-811098626/):

    included some (
    Stealing
    and (of some (
        Plans
        and (about some TIE-Interceptor)))
    and (participant value C1-10P))

[`Communication`](https://www.star-wars-ontology.co.uk/classes/708468623/) events are not an object but may also have a subject

eg [Escape_of_the_Engineer](https://www.star-wars-ontology.co.uk/individuals/1936970816/):

    included some ( Communication and
    about value Colossus and
    participant value First_Order and
    participant value Nenavakasa_Nalor)