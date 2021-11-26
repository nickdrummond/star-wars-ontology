# Test Questions

[back to index](index.md)

What can we ask of the Star Wars Universe we have modelled?

We have the following mechanisms available to us:
* Asserted facts
* Inferred facts
* Usage (includes backward references)
* DL query
* SparQL
* Sqwrl?
* Protege tooling
    * Individuals matrix
    * Ontograf


## Places
Transitivity on locations allows for containment:

    City and location value Outer_Rim = Cloud City etc

And querying for things that happened in a place:

    Event and location value Tatooine

## Where has Ezra been?
We can ask the locations of events at which Ezra (or the Spectres) were present:

    inverse(location) some (Event and participant some {Ezra_Bridger, Spectres})

Or planets that Ahsoka has been to:

    Planet and inverse(location) some (Event and participant value Ahsoka_Tano)

nb see [performance](performance.md) issues around making ```visited``` property chain

## Is Anakin from Tatooine?
Is Anakin from the Outer Rim? Get all beings from the Outer Rim:

    from some (Place and location value Outer_Rim)

## Membership of Rebels/Resistance etc?
Mixture of membership and roles
Handled by property chain on hadRole

    memberOf value Rebel_Alliance

Includes Holdo, who is not directly asserted to be a member
Also includes Ezra, who is a member of Spectres

But there are limitations. For example, we'd like to define Stormtroopers or CloneTroopers:

    hadRole some (Soldier and inOrganisation value Grand_Army_of_the_Republic)

Doesn't work for parts of org - ie doesn't catch Rex etc/ bad batch should include Rex, Hunter

We would need to have:

    (B) inOrganisation o memberOf -> inOrganisation

But, it is not compatible with the existing axiom:

    (A) hadRole o inOrganisation -> memberOf

  memberOf is just shorthand for hadRole X inOrganisation Y - maybe get rid at person level? But nice to say

  Someone has a role in an org and all its super-orgs

    Person --hadRole--> (Role --inOrg--> OrgX --memberOf(trans)--> OrgY)

  from (A)

    Person --memberOf--> OrgX
    Person --memberOf--> OrgY

  from (B)
  
    Person --hadRole--> inOrg --> OrgY

We could say the following but that would include an engineer for the Grand Army who defected to become a fighter for the Separatists

    Clone Trooper == (hadRole some Soldier) and (memberOf value GrandArmy...)

## Born/died before an event?
Can we do this relative to an event - ie better than "died some xsd:int[>0]"

## Who is related to Anakin Skywalker?
- Should include Ben Solo.
- Also includes Han Solo (as Ben's Father) as related is transitive. Should it?
- Rex is related to Jango Fett

## Who died during certain events?
    killedDuring some (Fight and on value Death_Star_1)

## Different individuals

If we add an allDifferent (and remove Darths/Rens) what else can we assert / query?

For a start, it grinds the reasoner to a halt
