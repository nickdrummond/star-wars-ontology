Todo

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
    * Ep 1 done
    * Ep 2 done
    * Ep 3 done
    * Ep 4 done
    * Ep 5 done
    * Ep 6 done
    * Ep 7 done
    * Ep 8 done
    * Ep 9 partial - SUDDENLY Murder inf = 65s - should be 13s
    * Rebel One partial - revise space battle over Scariff
    * Solo - escape from Corellia, train heist, Game - lando (falcon)

* Previous/next for events? What about overlap and will always need changing when "gaps" are filled in
It feels like we shouldn't need to retract facts really over time as new content comes out

* If we add an allDifferent (and remove Darths/Rens) what else can we assert / query?
For a start, it grinds the reasoner to a halt

* Homeworld for each species - might add a lot of places that aren't interesting in any other way

* Male and female? Not that important but would help with brother/sister/mother/father

* We can add images in seeAlso - jpg extension on anyURI. But, cannot manage copyright and hot loading
eg from Star Wars databank https://lumiere-a.akamaihd.net/v1/images/databank_zygerria_01_169_5cdad909.jpeg

* Can we have ABY/BBY instead of int as custom datatype?

* More clarity of groups in fights