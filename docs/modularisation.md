# Modularisation

## Issues

Cross over material - what level of duplication is ok?

Very difficult to extract characters to sub-modules as they are very "mobile" and/or inevitably relate
back to main individuals that must be in the core ontologies - We don't want to defeat our modelling principles of turning relations around in order to allow the modules to work

Perhaps only events can really be in the modules? 


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