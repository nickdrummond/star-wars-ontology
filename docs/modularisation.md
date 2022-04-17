# Modularisation

[back to index](index.md)

## Options for modules

* QA - eg domain/range checks
* out-of-universe metadata (actors, directors, creators)
* print/game/toy canon content
* deep content (more in-depth descriptions of species/planets etc)

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

Top level ontology importing all content (except species)

### base

Describes all Classes and Properties.
There are quite a small number of restrictions on classes at this level
as "Only a Sith deals in absolutes".

Avoids referencing instance data - no named individuals

Which means we want to avoid making statements like the following:

    All Mon Calamari are from Mon Cala

Instead, we can have the weaker (but probably more accurate):

    Mon Cala is the homeworld of Mon Calamari

### star-wars

Instance level information about characters, places, objects and a
small number of events that are widely referenced in the wider timeline.

A very small number of class level statements are made here - where the
axiom requires a reference to a named individual - eg:

    All Sith are inOrganisation Sith_Order
    All Clones are clones of Jango Fett
    All Imperial Star Destroyers are owned by the Empire


### Event ontologies

#### trilogy

Events from the original trilogy

* chapter IV, A New Hope 
* chapter V, The Empire Strikes Back
* chapter VI, Return of the Jedi

#### prequels

Events from the prequel trilogy

* chapter I, The Phantom Menace
* chapter II, Attack of the Clones
* chapter III, Revenge of the Sith

#### sequels

Events from the sequel trilogy

* chapter VII, The Force Awakens
* chapter VIII, The Last Jedi
* chapter IX, The Rise of Skywalker

#### rogue_one

Events from the film

#### solo

Events from the film

#### mandalorian

Events from 2 seasons

#### resistance

Events from 2 seasons. The first season especially was challenging to model in the existing framework as there is less direct conflict and a lot of the story is relationship driven. We can use this experience to expand some of the more subtle plot points in the rest of the Universe.

There are links to the timeline of the sequels as yet to be resolved. eg Hosnian Cataclysm.

#### rebels

Events from 4 seasons

#### clone_wars

Events from 7 seasons

#### bad_batch

Events from 1 season

### Eras

Grouping events by the timespan they occur during:

* republic-era
* imperial-era
* new-republic-era

### questions

Ontology containing some example defined classes that help to
classify individuals in the ontologies

### species

Deeper characterisation of species by physiology etc