# Modularisation

[back to index](index.md)

## Options for modules

* Events/content by series/film
* QA - eg domain/range checks
* out-of-universe metadata (actors, directors, creators)
* print/game/toy canon content


## Issues

This is a dynamic, large and forever changing domain.
Some of the benefits of modularising increase as the domain gets larger.

However, in order to keep things manageable, assertions should ideally not need to be retracted or moved once stated.

Therefore, we would like a module scheme that allows for characters/places/objects to show up in multiple places in future without having to extract them back out to the more general ontologies. 

Any character in a  sub-module is at risk of this as they are very "mobile" in the storytelling.
In addition, they inevitably relate back to "main" individuals that must be in the core ontologies -
this can defeat our modelling principles of turning relations around in order to allow the modules to work (eg parent/child)

Only events should be modelled outside the core modules? Even these are becoming more "mobile" as timelines are interlinked in the storytelling (eg The Hosnian Cataclysm).
We will then assert their links in the timeline of each module.

The only alternative is to accept constant moving or duplicating assertions in multiple places when characters cross over - but what level of duplication is ok?

If we get this right, the characters will be in the main ontology and then we'll
get a different "view" of them depending on which sub-modules we pull in.

## Structure

![Import Structure](imports.png)
    
## Modules

### all

Top level ontology importing all content

### base
Should include all Properties and Classes
Should avoid referencing instance data

Which means we cannot make statements like the following

    All Mon Calamari are from Mon Cala

Instead, we can have the weaker

    Mon Cala is the homeworld of Mon Calamari

Some statements require a type reference at the minimum

    All Sith are inOrganisation Sith_Order
    All Clones are clones of Jango Fett
    All Imperial Star Destroyers are owned by the Empire

### star-wars

Characters, places and a small number of events in the wider Universe

### Event ontologies

#### trilogy

#### sequels

#### prequels

#### rogue_one

#### solo

#### mandalorian

Characters and events from the series.

#### resistance

Events from the series. The first season especially was challenging to model in the existing framework as there is less direct conflict and a lot of the story is relationship driven. We can use this experience to expand some of the more subtle plot points in the rest of the Universe.

There are links to the timeline of the sequels as yet to be resolved. eg Hosnian Cataclysm.

#### rebels

Characters and events from the series.

#### clone_wars

#### bad_batch

### Eras

Grouping events by the timespan they occur during:

* republic-era
* imperial-era
* new-republic-era

### questions

Ontology containing 