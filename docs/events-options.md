[home](../) |
[docs](index.md) |
[benefits](benefits.md) |
[modules](modularisation.md) |
[events](events.md) |
[modelling principles](modelling-principles.md) |
[test questions](test-questions.md) |
[performance](performance.md) |
[tools](tools.md)

## Event implementation experiments

These were working notes as we worked out the patterns for representing
events. There are advantages and disadvantages of each approach.

We ended up using 3, modelling events as their own individuals, with
additional subclass assertions for sub-events and unnamed participants.

We still don't model groups that were operating together in a given event.

We also have open questions about the "no survivors" problem.
See [Torture_of_Shmi](https://star-wars-ontology.herokuapp.com/individuals/-631305730/).

### Options:
1. Simple property assertions on individuals
2. SubClass axioms on individuals
3. Named event individuals with property assertions
4. Named event with class expressions for grouping participants
5. hadRole version of 2
6. hadRole combination of 2 and 3
7. Named event, oneOf groupings as subclass
8. Use Team -> Organisation

### 1. Simple property assertions on individuals
A bit weak as it doesn't link the 2 things together or tell you how or where

Example

    Obi-Wan Kenobi killedBy Darth Vader
    Obi-Wan Kenobi died 0

### 2. SubClass axioms on individuals
We lose the simple inference that Darth Vader killed Obi Wan unless we put in property chains for these
and keep these other simpler properties

Example

    Obi-Wan -> killedDuring some (Duel and against value Darth_Vader and on value Death_Star_1 and date value 0)

But we can query for them:

    killedDuring some (date value 0)

    diedDuring some (Fight)

    diedDuring some (Fight and against value Darth_Vader)

    diedDuring some (Event on value Death_Star_1)

Can ask for events in a place? No, at these are not named events

    Event and on value Death_Star_1 = nothing unless we name the event (see below)

I suppose it shows up in the usage of Death Star though

### 3. Named event individuals with property assertions

Events can have a date, location, participants etc - more flexible use for battles, deaths, trades, abductions, anything?
This will allow more storytelling and linking to other events

Removes the event from Obi-Wan completely so need inference/usage to see

Lots more individuals!! (is it scalable?)

Example

    ObiDeath Type Duel 
        on DeathStar1
        victor Darth Vader
        killed Obi-Wan
        date 0

Nice other queries:
    
    diedDuring some Duel

    diedDuring some (Event and location value Death_Star_1)

    diedDuring some (date value 0)
    
    killedDuring some (Fight and victor value Maul)

    Event and on value Death_Star_1 = Ob1Death (must click through to find out what Ob1Death is)

This is what we're already doing for Battles though, so that's good

We don't want to make complicated names for every fight

We can still use option 2 and keep it local if the event is simple and not name worthy:

    Aurra_Sing -> diedIn some (Murder and victoryOf value Tobias_Beckett and date some xsd:int[> -21, < 10])

Can still query

    victorIn some (deathOf value Aurra_Sing)

### 4. Named event with class expressions for grouping participants

Anonymous groups can be created to show who fought who

But this has the same problem - no visibility in individuals where {} are used for multiple participants

Example

    Twilight of the Apprentice -> Fight
        duringWhich some {Ahsoka_Tano, Kanan_Jarrus, Ezra_Bridger, Chopper} and fought some {Seventh_Sister, Fifth_Brother}
        duringWhich some ({Maul} and fought some {Kanan_Jarrus, Ahsoka_Tano} and injurred some {Kanan_Jarrus})


### 5. hadRole version of 2

Same as 2 only reduces number of properties as we have Role classes instead - maybe if property hierarchy gets bloated?

Example

    Maul -> hadRole some (Victor and inEvent some (Fight and killed value Seventh_Sister and killed value Fifth_Brother))

### 6. hadRole combination of 2 and 3

Name the fight if it is more complex?

Example

    Maul -> hadRole some (Victor and inEvent some ("Twilight of the Apprentice" and killed value Seventh_Sister and killed value Fifth_Brother))

### 7. Named event, oneOf groupings as subclass

Doesn't scan - it's an event not a subclass of Maul - what would happen when individuals are disjoint?

Example

    "Twilight of the Apprentice" -> {Maul} and killed value Seventh_Sister

### 8. Use Team -> Organisation

This has the same probs as 4

In battles we can have:

    Team and hadMember Millenium Falcon
        and hadMember Nien_Numb
        and hadMember Lando
   
But then we lose the inverse connection to the Falcon etc


## Other requirements...

### Sub fights
How do we deal with skirmishes with multiple sub-fights? Eg Twilight of the Apprentice. We still want to capture Ahsoka vs Vader.
added including/during to have sub fights but then asking location or date gets messy:

    killedDuring some (Fight
        and (during some (location value Malachor_Sith_Temple)))

We want:

    killedDuring some (Fight
        and location value Malachor_Sith_Temple)

All possible properties (injured, survived etc) would need property chains - a bit heavy and not available for datatypes

Do we have to put a date on all sub-fights?

### No survivors

We can use deathOf on an organisation to denote that all members died.
This can be queried explicitly, keeping the classification light:

    Bodhi_Rook memberOf Rogue_One

    Battle_of_Scarrif -> killingOf Rogue_One

    Being and (
    (diedIn value Stealing_the_Death_Star_Plans) 
    or memberOf  some (diedIn value Stealing_the_Death_Star_Plans))

Its a bit clunky, especially if we want to include the super-event:

    Being and (
    (diedIn some (
        {Battle_of_Scarif} or (during value Battle_of_Scarif))
    or memberOf some (diedIn some (
        {Battle_of_Scarif} or (during value Battle_of_Scarif))
    )))

and it won't show up as an inference on the individual Beings

Feels expensive for a bit of shorthand modelling.

It also means opening up the Range of `diedIn` to include `Organisation`

#### Older attempts 


Rather than enumerating all individual deaths, can we do -

    Bodhi_Rook -> memberOf some (Rogue_One)
    survivor disjointWith died
    Battle_of_Scarif -> not (survivor some
        (memberOf some Rogue_One))

DL query:

    diedDuring value Battle_of_Scarif = Orson_Krennic

Should include the Rogue_One members? Why not??? Because Rogue_One is a class?

    survivor only (not (memberOf value Rogue_One))

No, that didn't work, but it does make it inconsistent to say one of them survived
This is because survivor and died are not covering?


    Battle_of_Scarrif -> killingOf Rogue_One 

This can be interpreted as killing of all members of the group

    memberOf o diedIn -> diedIn

But, this really slows reasoner down AND must remove "diedIn disjoint survived"

This works, but why is Jyn missed out? Its fine if we assert she is a member of Rogue One, but not if we infer it from:

    hadRole some (Fighter
        and (inOrganisation value Rogue_One))

Which does correctly infer the memberOf

It is not worth the huge reasoner overhead, so just enumerate the members
