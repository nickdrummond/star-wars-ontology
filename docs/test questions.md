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

## Membership of Organisations?
Mixture of membership and roles
Handled by property chain on hadRole

    memberOf value Rebel_Alliance

Includes Holdo, who is not directly asserted to be a member
Also includes Ezra, who is a member of Spectres

If we want to ask about who holds a specific role in an org, the following is incomplete - should include ```3-9```:

    hadRole some (Officer and inOrganisation value Galactic_Empire)

We need to query for the org and its parts:

    hadRole some (Officer and( inOrganisation some ({Galactic_Empire} or memberOf value Galactic_Empire )))

It makes sense to distinguish, as not all roles are equivalent when looking at the whole:
A leader of a unit does not make a leader of the army.

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
