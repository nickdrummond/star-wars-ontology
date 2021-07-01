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

Modularity
We could modularise our ontology into each episode/series - only exposing the facts as they are exposed
Nice to make assertions about Anakin and Darth Vader separately, but allow the inference to match




Todo

* Male and female? Not that important but would help with brother/sister/mother/father

* Referencing wookipedia, but should we refer to Star Wars databank as well/instead

* We can add images in seeAlso - jpg extension on anyURI. But, cannot manage copyright and hot loading
eg from Star Wars databank https://lumiere-a.akamaihd.net/v1/images/databank_zygerria_01_169_5cdad909.jpeg

* Can we have ABY/BBY instead of int as custom datatype?


* Sub fights
How do we deal with skirmishes with multiple sub-fights? Eg Twilight of the Apprentice. We still want to capture Ahsoka vs Vader.
added including/during to have sub fights but then asking location or date gets messy:
killedDuring some (Fight and (during some (location value Malachor_Sith_Temple)))

We want:
killedDuring some (Fight and location value Malachor_Sith_Temple)

All possible properties (injured, survived etc) would need property chains - a bit heavy and not available for datatypes

Do we have to put a date on all sub-fights?



* No survivors
Rather than enumerating all individual deaths, can we do -
Bodhi_Rook -> memberOf some (Rogue_One)
survivor disjointWith died

Battle_of_Scarif -> not (survivor some (memberOf some Rogue_One))

DL query: diedDuring value Battle_of_Scarif = Orson_Krennic

Should include the Rogue_One members? Why not??? Because Rogue_One is a class?

survivor only (not (memberOf value Rogue_One))

No, that didn't work