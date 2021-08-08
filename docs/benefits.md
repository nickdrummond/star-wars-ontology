# Advantages of modelling Star Wars in OWL

## Open World

This is an ongoing story with gaps being filled all the time.

It's good to have statements about what we know to be true but allowing all sorts of additional facts to become true

eg. Darth Vader sameAs Anakin Skywalker

## Inference

Using property hierarchy to infer relationship neighbourhoods

    knew value Anakin_Skywalker
    knew some (knew value Anakin_Skywalker)

## Graphs

Any property can be navigated as a graph

eg Event sequence (after/before) and containment (during)

![TROS timeline](events-TROS.png)

## Easy transitivity

Containment also very useful for locations

eg. Who was murdered in the Outer Rim?

Han killed Greedo in the Cantina in Mos Eisely on Tatooine in Outer Rim

eg. Shmi is related to Kylo Ren.

## Specificity

We have ways of modelling that can leave out unknown details or keep our level of commitment low.

People born somewhere in a range of years

Classification allows low-specificity
eg Role = Fighter or Trooper or Stormtrooper

eg No need to name everything (parents etc) - we can talk about Ezra's parents without having to name them.

## Modularity

We could modularise our ontology into each episode/series - only exposing the facts as they are exposed

It is nice to make assertions about Anakin and Darth Vader separately, but allow the inference to match

As the ontology gets larger, modules help with focus, although there is a tradeoff with the simplicity of one ontology