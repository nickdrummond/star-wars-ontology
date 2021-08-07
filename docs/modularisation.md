# Modularisation

## Issues

Cross over material - what level of duplication is ok?

## Structure

* base
    * star-wars
        * mandalorian
        * rebels
        * resistance
    
## Modules

### base
Should include all Properties and Classes
Should avoid referencing instance data

Currently several individuals referenced (search for hasValue):
* Rebel Scum (Rebel_Alliance, Resistance) - we could just get rid of this
* homeworlds - can we switch these round onto the Planets themselves
* Sith Lord memberOf
* Star Destroyers ownedBy
* Clone
* Fulcrum

### star-wars

Events in the wider Universe

### resistance

Characters and events from the series.

### rebels

Characters and events from the series.

### mandalorian

Characters and events from the series.

Not including more widely known characters - eg Ahsoka

Should planets that are only mentioned in the series also be in this ontology? - eg Nevarro