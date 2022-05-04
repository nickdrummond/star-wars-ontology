# Advantages of modelling Star Wars in OWL

## Open World

This is an ongoing story with gaps being filled all the time.

It's good to have statements about what we know to be true but allowing all sorts of additional facts to become true, eg:

    Darth Vader sameAs Anakin Skywalker

We can ask about who is Force sensitive:

    Human and (connectedTo some The_Force)

But, it also makes sense that we can't ask for those that aren't Force sensitive (it might just not be apparent, or intentionally hidden in the narrative):

    Human and not (connectedTo some The_Force)

It would be quite strong to assert someone was not force sensitive.

## Inference

Using property hierarchy to infer relationship neighbourhoods

    knew value Anakin_Skywalker
    knew some (knew value Anakin_Skywalker)

    relatedTo value Anakin_Skywalker

    Event and (participant some Flying_Creature)

## Consistency checking

Assertions must be consistent with the rest of the model

eg There can be no individual Female Crolute as all of the species are Male 

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

We have ways of modelling that can leave out unknown details or 
keep our level of commitment low.

Classification allows low-specificity:

eg hadRole some [Fighter](http://star-wars-ontology.herokuapp.com/classes/-147972479/) /
[Trooper](http://star-wars-ontology.herokuapp.com/classes/-342567613/) /
[Stormtrooper](http://star-wars-ontology.herokuapp.com/classes/251660378/) /
[Scout Trooper](http://star-wars-ontology.herokuapp.com/classes/-959023870/)

There is no need to name everything:

eg - we can talk about [Rey's](http://star-wars-ontology.herokuapp.com/individuals/-953035158/) parents without having to name them.
    
    hadFather some ( Human and wasCloneOf value Darth_Sidious)

eg People can be born in a range of years - eg [Din Djarin](http://star-wars-ontology.herokuapp.com/individuals/956466486/) was born before 19BBY

    born some int [<="-19"(int)]

## Modularity

An ontology built in modules allows:
* focused engineering - keep parts of each story together in one place -
eg [rogue one](http://star-wars-ontology.herokuapp.com/ontologies/2022284490/)
* optimisation - expensive modelling/reasoning can be separated off -
eg [hasPart](http://star-wars-ontology.herokuapp.com/objectproperties/-256302191/),
[hasTrait](http://star-wars-ontology.herokuapp.com/objectproperties/-822504072/)
* layering of knowledge - detail can be moved into other modules - 
eg [manufacturers](http://star-wars-ontology.herokuapp.com/objectproperties/-354745533/)
* visibility - out of Universe or non-public knowledge can be in their own models - eg behind-the-scenes.owl.ttl
* responsibility - ontologies could be extended by other fans

There is a tradeoff with the fluidity of developing one ontology

See [modularisation](modularisation.md) for more details.

## Referencing

We can reference any number of sources to our entities/assertions - currently [Wookipedia](https://starwars.fandom.com/)

We could reference images/videos - many ontology tools recognise common formats eg jpg.
But, we would need permissions on images and hosting or hot linking etc so this is out of scope.
