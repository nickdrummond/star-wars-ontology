Todo

* Remove all instances of killedByOLD (see refactor events below)
* Can we have ABY/BBY instead of int as custom datatype
* Need to make organisations into instances


* Refactor events to have a date, location, participants etc - more flexible use for battles, deaths, trades, abductions, anything?
This will allow more storytelling and linking to other events?
Eg death
OWK killedBy Darth Vader
OWK died 0
A bit crap as it doesn't link the 2 things together or tell you how or where

OB1-> killedDuring some (Duel and against value Darth_Vader and on value Death_Star_1 and date value 0)

But, we lose the simple inference that Darth Vader killed Obi Wan unless we put in property chains for these
and keep these other simpler properties

But we can query for them
killedDuring some (date value 0)
diedDuring some (Fight)
diedDuring some (against value Darth_Vader) - sounds weird
diedDuring some (on value Death_Star_1) - slightly weird

Can ask for events in a place? No, at these are not named events
Event and on value Death_Star_1 = nothing unless we name the event = lots more individuals!! (scalable?)
I suppose it shows up in the usage of Death Star though

Remove the event from OB1 completely too?

ObiDeath Type Duel
on DeathStar1
victor DV
killed Ob1
date 0

But then 
Event and on value Death_Star_1 = Ob1Death (doesn't really tell you much - we don't want to make complicated names for every fight??

This is what we're already doing for Battles though, so that's good

Nice other queries:
diedDuring some Duel
diedDuring some (Event and location value Death_Star_1)
diedDuring some (date value 0)
killedDuring some (Fight and victor value Maul)

How do we deal with skirmishes with multiple sub-fights? Eg Twilight of the Apprentice. We still want to capture Ahsoka vs Vader.
added including/during to have sub fights but then asking location or date gets messy:
killedDuring some (Fight and (during some (location value Malachor_Sith_Temple)))

We want:
killedDuring some (Fight and location value Malachor_Sith_Temple)

All possible properties (injured, survived etc) would need property chains - a bit heavy and not available for datatypes

Do we have to put a date on all sub-fights?

No survivors:
Rather than enumerating all individual deaths, can we do -

Bodhi_Rook -> memberOf some (Rogue_One)
survivor disjointWith died

Battle_of_Scarif -> not (survivor some (memberOf some Rogue_One))

DL query: diedDuring value Battle_of_Scarif = Orson_Krennic

Should include the Rogue_One members? Why not??? Because Rogue_One is a class?

survivor only (not (memberOf value Rogue_One))

No, that didn't work