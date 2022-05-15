# Advantages of modelling Star Wars in OWL

## OWL

OWL is the w3c standard for building ontologies for the Semantic Web.

The idea of the Semantic Web is to have a Worldwide web of data, traversable
and computable by computers. An alternative term is "Linked data".

An ontology is a knowledge model, containing classes, perhaps instances in 
a domain, and the properties that relate them.

OWL was developed using "Description Logic"
that can be computed over, giving us the benefits of inference and reasoning.
In OWL DL, all assertions are logical axioms that have a well understood
formal meaning.

It brings with it many benefits, on top of being a well understood standard.


## Inference

We can use a tool known as a reasoner to infer knowledge about our
Universe.

### Simple type inference

One of the simplest examples of this is type inference based on
the transitivity of subclasses.

    The Millenium Falcon is a YT-1300f_Light_Freighter
    YT-1300f_Light_Freighter is a subclass of Freighter
    Freighter is a subclass of Ship
    Ship is a subclass of Vehicle ... etc

Which we can use when reasoning about (querying) Vehicles, 

    Vehicle and ownedBy value Han_Solo

[result](http://star-wars-ontology.herokuapp.com/dlquery/?expression=Vehicle+and+ownedBy+value+Han_Solo%0D%0A&syntax=man)
includes the Millenium Falcon.


This works everywhere, within class descriptions

    Tobias_Beckett -> hadRole some Thief
    Thief is a subclass of Criminal

So we can ask for all Criminals

    hadRole some Criminal

[results](http://star-wars-ontology.herokuapp.com/dlquery/?expression=hadRole+some+Criminal&syntax=man)
include Beckett, thieves, smugglers, pirates and more

This gives us some freedom in modelling - see Specificity below

### Property inference

Using property hierarchy to infer relationship neighbourhoods

    knew value Anakin_Skywalker
    knew some (knew value Anakin_Skywalker)

    relatedTo value Anakin_Skywalker

    Event and (participant some Flying_Creature)

### Transitivity allows containment/inclusion

Transitivity is very useful for spatial, temporal and other
properties

eg. Who was killed in the Mid Rim?

    killedIn some (locatedIn value Mid_Rim)

[results](http://star-wars-ontology.herokuapp.com/dlquery/?expression=killedIn+some+%28locatedIn+value+Mid_Rim%29&syntax=man)
includes Qui-Gon Jinn who was killed in Theed, on Naboo, in the Mid Rim

eg. Who is related to Kylo Ren?

    relatedTo value Kylo_Ren

[results](http://star-wars-ontology.herokuapp.com/dlquery/?expression=relatedTo+value+Kylo_Ren&syntax=man)
includes Shmi

### Consistency checking

Assertions must be consistent with the rest of the model.

A reasoner lets us test this.

eg There can be no individual Female [Crolute](http://star-wars-ontology.herokuapp.com/classes/1749654878/) 
as all of the species are Male


## Open World

This is an ongoing story with gaps being filled all the time.

OWL implements an open world model where anything can be true until there is a statement
that makes that impossible.

For example, 2 individuals could be the same unless we make them distinct
explicitly or by inference (OWL does **not** make the "Unique Name Assumption").

### Unique Name Assumption (not made in OWL)

It feels odd to say Darth Vader won a podrace on Tatooine.
It's more intuitive to separate the two personas and talk about them in their
own contexts.
Then OWL allows us to state that Vader and Anakin are the same individual:

    Darth_Vader hadRole some Dark_Lord
    Capture_of_Tantive_IV participant Darth_Vader

    Anakin_Skywalker hadRole some Jedi_Knight
    Boonta_Eve_Classic_32BBY victoryOf Anakin_Skywalker

    Darth_Vader sameAs Anakin_Skywalker

### Negative assertions

We assert those that we know to be force sensitive so we can ask:

    Human and (connectedTo some The_Force)

But, if we ask for those that aren't Force sensitive:

    Human and not (connectedTo some The_Force)

We do not get any results.

This is because in OWL we would have to assert that someone
was not force sensitive - just omitting the information about
them being force sensitive is not enough.

In this Universe, it would be quite strong to assert someone was not force sensitive
- at what level is someone "sensitive"?
- it might just not be apparent within the stories we know
- or intentionally hidden by an individual

So, knowing someone is not force sensitive is not the same as
not knowing if they are force sensitive (maybe they don't know themselves)


## Properties create a network or graph

Any property can be navigated as a graph

eg Event sequence (after/before) and containment (during)

![TROS timeline](events-TROS.png)


## Specificity

We have ways of modelling that can leave out unknown details or 
keep our level of commitment low.

### Choosing a level of commitment

Classification allows control over how specific we wish to be:

eg killingOf some (hadRole some [Fighter](http://star-wars-ontology.herokuapp.com/classes/-147972479/) /
[Trooper](http://star-wars-ontology.herokuapp.com/classes/-342567613/) /
[Stormtrooper](http://star-wars-ontology.herokuapp.com/classes/251660378/) /
[Scout Trooper](http://star-wars-ontology.herokuapp.com/classes/-959023870/))

### Naming

There is no need to name everything:

eg - we can talk about [Rey's](http://star-wars-ontology.herokuapp.com/individuals/-953035158/) parents without having to name them.
    
    Rey -> hadFather some ( Human and wasCloneOf value Darth_Sidious)

### Ranges

eg People can be born in a range of years - eg [Din Djarin](http://star-wars-ontology.herokuapp.com/individuals/956466486/) was born before 19BBY

    Din -> born some int [<="-19"(int)]

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
* extensibility - responsibility for content can be distributed to other fans

There is a tradeoff with the fluidity of developing one ontology

See [modularisation](modularisation.md) for more details.

## Referencing

We can reference any number of sources to our entities/assertions - currently [Wookipedia](https://starwars.fandom.com/)

We could reference images/videos - many ontology tools recognise common formats eg jpg.
But, we would need permissions on images and hosting or hot linking etc so this is out of scope.
