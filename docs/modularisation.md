# Modularisation


## Issues

This is a dynamic, large and forever changing domain.
Some of the benefits of modularising increase as the domain gets larger.

However, in order to keep things manageable, assertions should ideally not need to be retracted or moved once stated.

Therefore, we would like a module scheme that allows for characters/places/objects to show up in multiple places in future without having to extract them back out to the more general ontologies. 

Any character in a  sub-module is at risk of this as they are very "mobile" in the storytelling.
In addition, they inevitably relate back to "main" individuals that must be in the core ontologies -
this can lead to us defeating our modelling principles of turning relations around in order to allow the modules to work (eg parent/child)

Therefore, only events should be modelled outside the core modules?

The only alternative is to accept constant moving or duplicating assertions in multiple places when characters cross over - but what level of duplication is ok?

If we get this right, the character will be in the main ontology and then we'll
get a different "view" of them depending on which sub-modules we pull in.

## Structure

![Import Structure](imports.png)
    
## Modules

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

Characters, places and events in the wider Universe

### resistance

Characters and events from the series.

### rebels

Characters and events from the series.

### mandalorian

Characters and events from the series.

Not including more widely known characters - eg Ahsoka

Should planets that are only mentioned in the series also be in this ontology? - eg Nevarro