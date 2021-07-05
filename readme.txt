Advantages of modelling Star Wars in OWL
------------------------------------

Open World
This is an ongoing story with gaps being filled all the time.
Its good to have statements about what we know to be true but allowing all sorts of additional facts to become true
Eg Darth Vader sameAs Anakin Skywalker

Inference
Using property hierarchy to infer relationship neighbourhoods
eg "knew value Anakin_Skywalker"
"knew some (knew value Anakin_Skywalker)"

Easy transitivity
Containment very useful for locations - Han killed Greedo in the Outer Rim - Cantina in Mos Eisely on Tatooine in Outer Rim
Shmi is related to Kylo Ren.

Specificity
People born before a date
Roles and other hierarchies allow low-specificity
No need to name everything (parents etc)

Modularity
We could modularise our ontology into each episode/series - only exposing the facts as they are exposed
Nice to make assertions about Anakin and Darth Vader separately, but allow the inference to match



Todo

* First round of events for each film
    * Ep 1 done
    * Ep 2 done
    * Ep 3 done
    * Ep 4 partial - break down Battle of Yavin?
    * Ep 5
    * Ep 6 partial - need speeder bike event? Capture by ewoks?
    * Ep 7
    * Ep 8 done - maybe add mutiny on the Raddus?
    * Ep 9
    * Rebel One
    * Solo

* All members of Rogue One were killed at Scariff

BoS killingOf Rogue_One (can be interpreted as killing of all members of the group)
memberOf o diedIn -> diedIn (really slows reasoner down AND must remove "diedIn disjoint survived")
This works, but why is Jyn missed out? Its fine if we assert she is a member of Rogue One, but not if we infer it from:
hadRole some (Fighter and (inOrganisation value Rogue_One)) which does correctly infer the memberOf
Is it worth it or just enumerate the members?

* If we add an allDifferent (and remove Darths/Rens) what else can we assert / query?
For a start, it grinds the reasoner to a halt

* Homeworld for each species

* Male and female? Not that important but would help with brother/sister/mother/father

* Referencing wookipedia, but should we refer to Star Wars databank as well/instead

* We can add images in seeAlso - jpg extension on anyURI. But, cannot manage copyright and hot loading
eg from Star Wars databank https://lumiere-a.akamaihd.net/v1/images/databank_zygerria_01_169_5cdad909.jpeg

* Can we have ABY/BBY instead of int as custom datatype?

* Beat/ beatBy eg LS beat Vader but not killed

* More clarity of groups in fights