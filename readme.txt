Todo

* Need a test framework
- consistency check YUP
- classification time YUP
- DL query time - Murder - YUP
- DL queries for expected results
- Quality checks - eg seeALso annotations - YUP

* Queries going v slow
Event and location value Outer_Rim = 4s
killedIn some Murder = ~20s
Removing all humans = 10s
Removing all events except Murder =10s
But even removing all other mentions of Murder does no better
Deleted all locations and organisations = 5s
Deleted just orgs = 12s
locs = 9s?
COmmits + ConsistencyTest
01/07 11:18 = 5.2 5.3 5.3s
01/07 13:43 = 5.6 6.0 5.4 5.5s
01/07 21:10 = 6.0 6.5 6.3s
02/07 00:18 = 7.5 7.5 7.1
02/07 23:50 = 9.3 9.4 8.6
03/07 17:52 = 10 10 10
04/07 15:38 = 10 10s
04/07 23:50 = 11 10s
05/07 00:19 = 19 19
05/07 22:06 = 21 22s
12/07 21:07 = 26.0 27.0
head = 12.5 13.4 12.6


removed from/on chain = 12.3 12.7 12.3 no difference
removed hadRole/member chain = 13.2 13 12 no difference



* Incorrect use of participant/deathOf etc - using Role - eg participant some Stormtrooper
should be participant some (hadRole some Stormtrooper)

Review all properties:
* married not used
* saved not used
* is of even defined properly?
* visited only used once - useful?

* First round of events for each film
    * Ep 1 done
    * Ep 2 done
    * Ep 3 done
    * Ep 4 partial - break down Battle of Yavin?
    * Ep 5 partial - Bespin Han/rescue?
    * Ep 6 partial - need speeder bike event? Capture by ewoks?
    * Ep 7 partial - escape from Jakku
    * Ep 8 done?
    * Ep 9 partial - Pasaana, finding wayfinder - taking chewie
    * Rebel One partial - revise space battle over Scariff
    * Solo - escape from Corellia, train heist, Game - lando (falcon)

* All members of Rogue One were killed at Scariff

BoS killingOf Rogue_One (can be interpreted as killing of all members of the group)
memberOf o diedIn -> diedIn (really slows reasoner down AND must remove "diedIn disjoint survived")
This works, but why is Jyn missed out? Its fine if we assert she is a member of Rogue One, but not if we infer it from:
hadRole some (Fighter and (inOrganisation value Rogue_One)) which does correctly infer the memberOf
Is it worth it or just enumerate the members?

* Previous/next for events? What about overlap and will always need changing when "gaps" are filled in
It feels like we shouldn't need to retract facts really over time as new content comes out

* If we add an allDifferent (and remove Darths/Rens) what else can we assert / query?
For a start, it grinds the reasoner to a halt

* Homeworld for each species - might add a lot of places that aren't interesting in any other way

* Male and female? Not that important but would help with brother/sister/mother/father

* We can add images in seeAlso - jpg extension on anyURI. But, cannot manage copyright and hot loading
eg from Star Wars databank https://lumiere-a.akamaihd.net/v1/images/databank_zygerria_01_169_5cdad909.jpeg

* Can we have ABY/BBY instead of int as custom datatype?

* Beat/ beatBy eg LS beat Vader but not killed

* More clarity of groups in fights