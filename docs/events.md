[home](../) |
[docs](index.md) |
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
An extract of [Escape_from_Death_Star](https://star-wars-ontology.herokuapp.com/individuals/-290302611/):

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
            killingOf some (hadRole some StormTrooper)
        ...

## Event types

Type is a primary classification, represented by subclasses of [Event](https://star-wars-ontology.herokuapp.com/classes/-885778338/).
Examples include everything from epic [Battles](https://star-wars-ontology.herokuapp.com/classes/1029558972/)
to a game of [Dejarik](https://star-wars-ontology.herokuapp.com/classes/-2034936224/).

## Time

### Absolute

We are only able to specify the [year](https://star-wars-ontology.herokuapp.com/dataproperties/-949412319/))
in which events take place using BBY/ABY where negative integers are BBY.

### Relative

#### Before/after

We link events together creating a timeline using before/[after](https://star-wars-ontology.herokuapp.com/objectproperties/-860382272/).
There is a transitive sometimeBefore/[sometimeAfter](https://star-wars-ontology.herokuapp.com/objectproperties/149909119/) that allows for non-contiguous modelling and reasoning about the order in which things happen.

See sub-events below for events that happen during other events

#### During

We create named events if they are significant enough or we want to refer to them again and
then simply link them to the main event with [```during```](https://star-wars-ontology.herokuapp.com/objectproperties/2021350437/).

eg [Ahsoka_vs_Vader](https://star-wars-ontology.herokuapp.com/individuals/1661983043/) happened during
[Twilight_of_the_Apprentice](https://star-wars-ontology.herokuapp.com/individuals/-1120064623/)

#### Included

We include the "parts" of an event in the description of the main event by adding subclasses
using restrictions on the ```included``` property.

This almost gives us the starting point for building a DSL style of modelling:

    Order_66 -> included some (Murder and (of value Aayla_Secura) and (locatedIn value Felucia))


## Location

Locations can be very specific, at the level of a room, or as wide as a [Planet](https://star-wars-ontology.herokuapp.com/classes/1439953820/).

Some are named. eg [Aunt_Z's_Tavern ](https://star-wars-ontology.herokuapp.com/individuals/-2012059427/), 
[Saleucami ](https://star-wars-ontology.herokuapp.com/individuals/-1427370943/)

Many locations mentioned in events are not modelled as individuals as there is
no point in naming them:

    locatedIn some ( Garbage_Compactor and (locatedIn value Death_Star_1)))

This uses the Class [Garbage_Compactor](https://star-wars-ontology.herokuapp.com/classes/-1960824747/) qualified by its location.


## Participants

Beings, ships (place) and the Force can all be involved in an event.
In fact, the range of the [participant](https://star-wars-ontology.herokuapp.com/objectproperties/1712213772/) property is:

    Place or Object or Actor

There are also [objects that are participants](https://star-wars-ontology.herokuapp.com/dlquery/?expression=Object+and+participatedIn+some+Event&syntax=man&query=instances).
This is because [destructionOf](https://star-wars-ontology.herokuapp.com/objectproperties/-1041073662/) and other properties
that are used with objects are subproperties of participant. We also see
frequent [Sabotage](https://star-wars-ontology.herokuapp.com/classes/-1625575009/) of objects and the
[of](https://star-wars-ontology.herokuapp.com/objectproperties/944795056/) property also implies participation.
See this [ticket about use of "of"](https://github.com/nickdrummond/star-wars-ontology/issues/15).

Participants may be property assertions referencing named individuals

    Torture_of_Shmi participant Anakin_Skywalker

or, frequently, we want to specify a group of anonymous participants
such as stormtroopers. We have to be remember that [StormTrooper](https://star-wars-ontology.herokuapp.com/classes/-2145398193/)
is a role, fulfilled by a person. The range of participant requires an [Actor](https://star-wars-ontology.herokuapp.com/classes/1007884718/)
such as a Human which is why we see the common pattern:

    killingOf some (hadRole some StormTrooper)

[This happens a lot](https://star-wars-ontology.herokuapp.com/dlquery/?expression=killingOf+some+%28hadRole+some+StormTrooper%29&syntax=man&query=instances)
or even more if you [include sub-events](https://star-wars-ontology.herokuapp.com/dlquery/?expression=%28killingOf+some+%28hadRole+some+StormTrooper%29%29+or+%28included+some+%28killingOf+some+%28hadRole+some+StormTrooper%29%29%29&syntax=man&query=instances)

The following would be too specific - are all StormTroopers human?

    killingOf some (Human and hadRole some StormTrooper)


## Development

Also see [development notes](events-options.md) when we went through modelling options.