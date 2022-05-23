[docs](index.md) |
[benefits](benefits.md) |
[modules](modularisation.md) |
[events](events.md) |
modelling principles |
[test questions](test-questions.md) |
[performance](performance.md) |
[tools](tools.md)

# General modelling principles

## References

- should use [`seeAlso`](http://star-wars-ontology.herokuapp.com/annotationproperties/-1773693006/) to reference all Individuals in Wookipedia
- should reference all non-obvious Classes in Wookipedia

## Properties

- should name properties in past tense "A long time ago..."
- should be used more than once otherwise they need reviewing
- should be asserted in one direction, inverses being inferred

eg [`after`](http://star-wars-ontology.herokuapp.com/objectproperties/1037526453/) shows usage but 
[`before`](http://star-wars-ontology.herokuapp.com/objectproperties/-448094376/) has no assertions


## Disjoints/differentFrom

- should have top level disjoints to assist clean modelling and aid property use consistency checking
- should not use differentFrom because of its impact on [performance](performance.md)

## Negation

- should use negation sparingly as it is a strong assertion at risk of being wrong - eg "no survivors"

eg [Torture_of_Shmi](http://star-wars-ontology.herokuapp.com/individuals/1765752841/)

    not (survivedBy some (Tusken and originallyFrom value Tusken_Raider_camp))

## Event focussed

* should provide a [`year`](http://star-wars-ontology.herokuapp.com/dataproperties/948496406/) for all named events, if known
  * negative numbers are BBY, positive are ABY  
- may attach involvement in an event to the [`Actor`](http://star-wars-ontology.herokuapp.com/classes/1007884718/), if there is no natural named Event to use

eg [Ochi](http://star-wars-ontology.herokuapp.com/individuals/-950665255/)
    
    participatedIn some (Murder and
    deathOf some (hadChild value Rey))

###  Timeline

- should place events in the timeline using `eventB after eventA`, if known
- should use [`someTimeAfter`](http://star-wars-ontology.herokuapp.com/objectproperties/2047817844/) when larger time gaps between events (eg between series)
- should use [`during`](http://star-wars-ontology.herokuapp.com/objectproperties/-375708134/) between named events - not its inverse
- should use [`included`](http://star-wars-ontology.herokuapp.com/objectproperties/1035051157/) in subclassOf restrictions on event if its not worth naming

eg [Escape_from_the_Garbage_Compactor](http://star-wars-ontology.herokuapp.com/individuals/733282715/):

    included some(Attack 
    and (participant some Dianoga)
    and (survivedBy value Luke_Skywalker))

## Beings

- primary classification is species.
- should assert relations between beings on the one it affects most - eg `Luke trainedBy Obi-wan` 
unless an [`Event`](http://star-wars-ontology.herokuapp.com/classes/1012130387/) is called for
- should all have at least one [`hadRole`](http://star-wars-ontology.herokuapp.com/objectproperties/1627826554/) (whether or not in an organisation)


## from (where born/grew up)

- should use [`originallyFrom`](http://star-wars-ontology.herokuapp.com/objectproperties/-1044081727/) at instance level where people originate
- should use [`homeworldOf`](http://star-wars-ontology.herokuapp.com/objectproperties/418614051/) on Planets/Moons for a weaker reference to species (also works with modularisation as species are Classes).
- should use [`livedIn`](http://star-wars-ontology.herokuapp.com/objectproperties/1129084950/) for any other location someone spent their time 

## The Force

[`Force_spirits`](http://star-wars-ontology.herokuapp.com/classes/1763189694/),
[`Force_bonds`](http://star-wars-ontology.herokuapp.com/classes/-1223412816/)
and [`connections`](http://star-wars-ontology.herokuapp.com/objectproperties/-1625702595/)
are manifestations of [`The Force`](http://star-wars-ontology.herokuapp.com/classes/-1757453002/),
not the individual.

[Search_for_Luke](http://star-wars-ontology.herokuapp.com/individuals/-977991436/):

    participant some (Force_spirit
        and (connectedTo value Obi-Wan_Kenobi))

[Attack_on_Phoenix_Squadron](http://star-wars-ontology.herokuapp.com/individuals/140937268/):

    participant some (Force_Bond
        and (connectedTo value Ahsoka_Tano)
        and (connectedTo value Darth_Vader))
    
[Rey_vs_Kylo_DeathStar](http://star-wars-ontology.herokuapp.com/individuals/-934572091/):

    participant some (The_Force
        and (connectedTo value Leia_Organa))

[`Force visions`](http://star-wars-ontology.herokuapp.com/classes/926769109/) are also manifestations of the force, connected to the individual experiencing
them. The content of the vision is described using [`about`](http://star-wars-ontology.herokuapp.com/objectproperties/1037402982/)

eg [Evacuation_of_Garel](http://star-wars-ontology.herokuapp.com/individuals/-816060537/)

    participant some (Force_Vision
        and (connectedTo value Ezra_Bridger)
        and (about some (parentOf value Ezra_Bridger))
        and (about value White_Loth_Cat))

## Information

[`Information`](http://star-wars-ontology.herokuapp.com/classes/286293221/) is
treated as an object in the Universe, can have a subject and can be 
[`traded`](http://star-wars-ontology.herokuapp.com/objectproperties/79076185/) or
[`found`](http://star-wars-ontology.herokuapp.com/objectproperties/1042413403/).

eg [Meeting_Cid ](http://star-wars-ontology.herokuapp.com/individuals/-168351138/):

    traded some (Information and about value Fennec_Shand)

We might be interested in the form of the information

eg [Infiltrating_the_Imperial_Armoury ](http://star-wars-ontology.herokuapp.com/individuals/1585959945/):

    included some (
    stole some (Plans and about some TIE-Interceptor) and
    participant value C1-10P)

[`Communication`](http://star-wars-ontology.herokuapp.com/classes/708468623/) events are not an object but may also have a subject

eg [Escape_of_the_Engineer](http://star-wars-ontology.herokuapp.com/individuals/39062091/):

    included some ( Communication and
    about value Colossus and
    participant value First_Order and
    participant value Nenavakasa_Nalor)