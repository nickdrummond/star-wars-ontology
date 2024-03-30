[home](../) |
[browse](https://www.star-wars-ontology.co.uk/) |
[docs](readme.md)

[benefits](benefits.md) |
modules |
[events](events.md) |
[modelling principles](modelling-principles.md) |
[test questions](test-questions.md) |
[performance](performance.md) |
[tools](tools.md)

# Modularisation

All ontology modules available in Turtle format in the [ontologies](../ontologies/) directory.

## Structure

![Import Structure](imports.png)
    
## Modules

### [all](https://www.star-wars-ontology.co.uk/ontologies/-1715300141/)

Top level ontology importing all content.

### [base](https://www.star-wars-ontology.co.uk/ontologies/-1190915901/)

Describes all Classes and Properties.
There are quite a small number of restrictions on classes at this level
as "only a Sith deals in absolutes".

Avoids referencing instance data - no named individuals

Which means we want to avoid making statements like "Mon Calamari are all from Mon Cala":

    Mon_Calamari subclassOf (originallyFrom value Mon_Cala)

Instead, we can have the weaker (but probably more accurate) definition of [Mon_Cala](https://www.star-wars-ontology.co.uk/individuals/669928383/):

    Mon_Cala type (homeworldOf some Mon_Calamari)

### [star-wars](https://www.star-wars-ontology.co.uk/ontologies/-745736692/)

Instance level information about characters, places, objects and a
small number of events that are widely referenced in the wider timeline.

A very small number of class level statements are made here - where the
axiom requires a reference to a named individual - eg:

    All Dark_Lords are inOrganisation Sith_Order
    All Clones are clones of Jango Fett
    All Imperial Star Destroyers are owned by the Empire

see [Dark_Lord](https://www.star-wars-ontology.co.uk/classes/1095482871/), 
[Clone](https://www.star-wars-ontology.co.uk/classes/1009995030/),
[Imperial-class_Star_Destroyer](https://www.star-wars-ontology.co.uk/classes/2098826796/)


### Event ontologies

#### [events](https://www.star-wars-ontology.co.uk/ontologies/-16665301/)

Groups all events by importing all eras.

This is the ontology to load if you want to reason over events as the peripheral
ontologies with expensive modelling are not included.

#### [trilogy](https://www.star-wars-ontology.co.uk/ontologies/-1571907858/)

Events from the original trilogy

* chapter IV, A New Hope 
* chapter V, The Empire Strikes Back
* chapter VI, Return of the Jedi

#### [prequels](https://www.star-wars-ontology.co.uk/ontologies/1025857927/)

Events from the prequel trilogy

* chapter I, The Phantom Menace
* chapter II, Attack of the Clones
* chapter III, Revenge of the Sith

#### [sequels](https://www.star-wars-ontology.co.uk/ontologies/-794547428/)

Events from the sequel trilogy

* chapter VII, The Force Awakens
* chapter VIII, The Last Jedi
* chapter IX, The Rise of Skywalker

#### [rogue_one](https://www.star-wars-ontology.co.uk/ontologies/-769536717/)

Events from the film

#### [solo](https://www.star-wars-ontology.co.uk/ontologies/198972105/)

Events from the film

#### [mandalorian](https://www.star-wars-ontology.co.uk/ontologies/1284360452/)

Events from 2 seasons

#### [resistance](https://www.star-wars-ontology.co.uk/ontologies/-1881387829/)

Events from 2 seasons. The first season especially was challenging to model in the existing framework as there is less direct conflict and a lot of the story is relationship driven. We can use this experience to expand some of the more subtle plot points in the rest of the Universe.

There are links to the timeline of the sequels as yet to be resolved. eg Hosnian Cataclysm.

#### [rebels](https://www.star-wars-ontology.co.uk/ontologies/-530806561/)

Events from 4 seasons

#### [clone_wars](https://www.star-wars-ontology.co.uk/ontologies/1278562005/)

Events from 7 seasons

#### [bad_batch](https://www.star-wars-ontology.co.uk/ontologies/-893076728/)

Events from 1 season

### Eras

Grouping events by the timespan they occur during:

* [republic-era](https://www.star-wars-ontology.co.uk/ontologies/-1501389091/)
* [imperial-era](https://www.star-wars-ontology.co.uk/ontologies/1616560536/)
* [new-republic-era](https://www.star-wars-ontology.co.uk/ontologies/-768890178/)

### Peripheral/deeper ontologies

#### [species](https://www.star-wars-ontology.co.uk/ontologies/650255162/)

Deeper characterisation of species by physiology/traits etc.

**Warning** - loading this ontology slows classification quite substantially

#### [manufacturers](https://www.star-wars-ontology.co.uk/ontologies/1073412504/)

Characterisation of places/vehicles/droids and objects by who created them.

**Warning** - loading this ontology slows classification quite substantially

#### questions

Ontology containing some example defined classes that help to
classify individuals in the ontologies

#### behind-the-scenes

Stub ontology demonstrating that we can model out of Universe knowledge, such as people involved in the making
of the films/TV series and other aspects.

## Other options

* QA - eg domain/range checks
* print/game/toy canon content
* more deep content (more in-depth descriptions of characters/planets etc)

## Discussion

This is a dynamic, large and forever changing domain.
Some of the benefits of modularising increase as the domain gets larger.

However, in order to keep things manageable, assertions should ideally not need to be retracted or moved once stated.

Therefore, we would like a module scheme that allows for characters/places/objects to show up in multiple places in future without having to extract them back out to the more general ontologies.

Any character in a  sub-module is at risk of this as they are very "mobile" in the storytelling.
In addition, they inevitably relate back to "main" individuals that must be in the core ontologies -
this can defeat our modelling principles of turning relations around in order to allow the modules to work (eg parent/child)

Only events should be modelled outside the core modules? Even these are becoming
more "mobile" as timelines are interlinked in the storytelling
(eg The [`Hosnian Cataclysm`](https://www.star-wars-ontology.co.uk/individuals/1317043629/)).
We will then assert their links in the timeline of each module.

The only alternative is to accept constant moving or duplicating assertions in multiple places when characters cross over - but what level of duplication is ok?

If we get this right, the characters will be in the main ontology and then we'll
get a different "view" of them depending on which sub-modules we pull in.
