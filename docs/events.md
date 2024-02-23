[home](../) |
[browse](https://star-wars-ontology.up.railway.app/) |
[docs](readme.md)

[benefits](benefits.md) |
[modules](modularisation.md) |
events |
[modelling principles](modelling-principles.md) |
[test questions](test-questions.md) |
[performance](performance.md) |
[tools](tools.md)

# Events

Events are the storytelling piece that binds the people, places and things together (much like the Force).

We model several aspects of an event:
- type
- time
    - absolute 
    - relative 
- location
- participants

## Example
An extract of [Escape_from_Death_Star](https://star-wars-ontology.up.railway.app/individuals/-290302611/):

    Escape_from_Death_Star:
        year 0
        after The_Disaster
        during Galactic_Civil_War
        locatedIn Death_Star_1
        participant C-3PO
        participant R2-D2

        types:
            Escape
            included some (
                (deactivated some Tractor_Beam_Projector) and
                (participant value Obi-Wan_Kenobi))
            )
            included some ( Killing and (of some (hadRole some StormTrooper)))
        ...

## Event types

Type is a primary classification, represented by subclasses of [Event](https://star-wars-ontology.up.railway.app/classes/1012130387/).
Examples include everything from epic [Battles](https://star-wars-ontology.up.railway.app/classes/-1367499599/)
to a game of [Dejarik](https://star-wars-ontology.up.railway.app/classes/-137027499/).

## Time

### Absolute

We are only able to specify the [year](https://star-wars-ontology.up.railway.app/dataproperties/948496406/))
in which events take place using BBY/ABY where negative integers are BBY.

### Relative

#### Before/after

We link events together creating a timeline using before/[after](https://star-wars-ontology.up.railway.app/objectproperties/1037526453/).
There is a transitive sometimeBefore/[sometimeAfter](https://star-wars-ontology.up.railway.app/objectproperties/2047817844/) that allows for non-contiguous modelling and reasoning about the order in which things happen.

See sub-events below for events that happen during other events

#### During

We create named events if they are significant enough or we want to refer to them again and
then simply link them to the main event with [```during```](https://star-wars-ontology.up.railway.app/objectproperties/-375708134/).

eg [Ahsoka_vs_Vader](https://star-wars-ontology.up.railway.app/individuals/1661983043/) happened during
[Twilight_of_the_Apprentice](https://star-wars-ontology.up.railway.app/individuals/-1120064623/)

#### Included

We include the "parts" of an event in the description of the main event by adding subclasses
using restrictions on the ```included``` property.

This almost gives us the starting point for building a DSL style of modelling:

    Order_66 -> included some (Murder and (of value Aayla_Secura) and (locatedIn value Felucia))


## Location

Locations can be very specific, at the level of a room, or as wide as a [Planet](https://star-wars-ontology.up.railway.app/classes/-957104751/).

Some are named. eg [Aunt_Z's_Tavern ](https://star-wars-ontology.up.railway.app/individuals/-2012059427/), 
[Saleucami ](https://star-wars-ontology.up.railway.app/individuals/-1427370943/)

Many locations mentioned in events are not modelled as individuals as there is
no point in naming them:

    locatedIn some ( Garbage_Compactor and (locatedIn value Death_Star_1)))

This uses the Class [Garbage_Compactor](https://star-wars-ontology.up.railway.app/classes/-1960824747/) qualified by its location.


## Participants

Beings, ships (place) and the Force can all be involved in an event.
In fact, the range of the [participant](https://star-wars-ontology.up.railway.app/objectproperties/1712213772/) property is:

    Place or Object or Actor

There are also [objects that are participants](https://star-wars-ontology.up.railway.app/dlquery/?expression=Object+and+participatedIn+some+Event&syntax=man&query=instances).
This is because events such as [Sabotage](https://star-wars-ontology.up.railway.app/classes/-1625575009/)
and [Destruction](https://star-wars-ontology.up.railway.app/classes/782662763/) use
[of](https://star-wars-ontology.up.railway.app/objectproperties/944795056/) which is a subproperty of ```participant```.
See this [ticket about use of "of"](https://github.com/nickdrummond/star-wars-ontology/issues/15).

Participants may be property assertions referencing named individuals

    Torture_of_Shmi participant Anakin_Skywalker

or, frequently, we want to specify a group of anonymous participants
such as stormtroopers. We have to be remember that [StormTrooper](https://star-wars-ontology.up.railway.app/classes/-2145398193/)
is a role, fulfilled by a person. The range of participant requires an [Actor](https://star-wars-ontology.up.railway.app/classes/1007884718/)
such as a Human which is why we see the common pattern:

    included some (Killing and (of some (hadRole some StormTrooper)))

[This happens a lot](https://star-wars-ontology.up.railway.app/dlquery/?expression=included+some+%28Killing+and+%28of+some+%28hadRole+some+StormTrooper%29%29%29&minus=&syntax=man&query=instances)

The following would be too specific - are all StormTroopers human?

    included some (Killing and (of some (Human and hadRole some StormTrooper)))


## Development

Also see [development notes](events-options.md) when we went through modelling options.