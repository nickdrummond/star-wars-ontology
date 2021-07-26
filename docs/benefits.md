# Advantages of modelling Star Wars in OWL

## Open World
* This is an ongoing story with gaps being filled all the time.
* Its good to have statements about what we know to be true but allowing all sorts of additional facts to become true

eg. Darth Vader sameAs Anakin Skywalker

## Inference
* Using property hierarchy to infer relationship neighbourhoods

eg. "knew value Anakin_Skywalker"
"knew some (knew value Anakin_Skywalker)"

## Easy transitivity
* Containment very useful for locations

eg. Han killed Greedo in the Outer Rim - Cantina in Mos Eisely on Tatooine in Outer Rim

eg. Shmi is related to Kylo Ren.

## Specificity
* People born before a date
* Roles and other hierarchies allow low-specificity
* No need to name everything (parents etc)

## Modularity
* We could modularise our ontology into each episode/series - only exposing the facts as they are exposed
* Nice to make assertions about Anakin and Darth Vader separately, but allow the inference to match

