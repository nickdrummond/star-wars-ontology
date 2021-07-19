Todo

* How can we best use SPARQL and SQWRL?
https://github.com/protegeproject/swrlapi/wiki/SWRLLanguageFAQ


Someone who was killed during an event died on the date of that event
- need to infer "died" from event
- eg Plo Koon died in Order 66 on 19BBY
- doesn't look like you can use property chains for datatype properties
- Can we use SWRL?

https://github.com/protegeproject/swrlapi/wiki/SWRLLanguageFAQ
https://drive.google.com/file/d/1Ofk0HxmJdKspsSAmzOdB9PsXpvR3nJqv/view


Sentient(?person) ^ diedIn(?person, ?event) ^ year(?event, ?y) -> diedInYear(?person, ?y)
SWRL tab freezes up when we try to run it
Reduced the number of instances to just Attack_on_the_Raddus + Leia + Ackbar
It runs the rule pretty much instantly but you have to assert them back into the ontology with the SWRL tab - clunky

If we just want to query
https://github.com/protegeproject/swrlapi/wiki/SQWRL
Sentient(?person) ^ diedIn(?person, ?event) ^ year(?event, ?y) -> sqwrl:select(?person, ?y) ^ sqwrl:orderBy(?y)

This works fine again in the simple case, but looks like Drools can't handle the number of individuals



* Improve classification times - currently 24s for Murder inference

* do we want useful defs like StormTrooper == hadRole some (Soldier and inOrg ...)?
  doesn't work for parts of org - ie doesn't catch Rex etc/ bad batch
  hadRole some (Soldier and inOrganisation value Grand_Army_of_the_Republic)
   should include Rex, Hunter

   (A) hadRole o inOrganisation -> memberOf  cannot be used with
   (B) inOrganisation o memberOf -> inOrganisation

   memberOf is just shorthand for hadRole X inOrganisation Y - maybe get rid at person level? But nice to say

   Someone has a role in an org and all its super-orgs
   Person --hadRole--> (Role --inOrg--> OrgX --memberOf(trans)--> OrgY)

   from (A)
   Person --memberOf--> OrgX
   Person --memberOf--> OrgY

   from (B)
   Person --hadRole--> inOrg --> OrgY

   we could say Clone Trooper == (hadRole some Soldier) and (memberOf value GrandArmy...)
   but that would include an engineer for the Grand Army who defected to become a fighter for the Separatists

Maybe not - maybe it should be the reverse - Nien Numb was a pilot of Tantive when he died


Review all properties:
* is of even defined properly?
* visited only used once - useful?

* First round of events for each film
    * Solo - escape from Corellia, train heist, Game - lando (falcon)

* Force Ghosts/Connections
participant some (Force_spirit and (connectedTo value Obi-Wan_Kenobi))
participant some (The_Force and (connectedTo value Leia_Organa))

* Revise classification of living things - sentience makes it difficult to classify by type (eg Reptile)

* Previous/next for events? What about overlap and will always need changing when "gaps" are filled in
It feels like we shouldn't need to retract facts really over time as new content comes out

* If we add an allDifferent (and remove Darths/Rens) what else can we assert / query?
For a start, it grinds the reasoner to a halt

* Homeworld for each species - might add a lot of places that aren't interesting in any other way

* We can add images in seeAlso - jpg extension on anyURI. But, cannot manage copyright and hot loading
Unfortunately, probably no pattern to generate automatically
eg from Star Wars databank https://lumiere-a.akamaihd.net/v1/images/databank_zygerria_01_169_5cdad909.jpeg

* Can we have ABY/BBY instead of int as custom datatype?

* More clarity of groups in fights