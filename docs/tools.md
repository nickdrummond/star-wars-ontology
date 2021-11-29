# Tools

[back to index](index.md)

## Protege

We used [Protege](https://protege.stanford.edu) to build this ontology

![Star Wars ontology loaded in Protege](killing_a_Krayt_Dragon.png)

### Reasoning

Use the Pellet reasoner

* Select `Pellet` from the `Reasoner` menu.
* Select `Start Reasoner` to show inferences in the model

Other reasoners are available, but each has different [performance](performance.md) issues

### Protege Known Issues

* Using Turtle as a file format means changes to base cannot be saved if other ontologies loaded 
  * bug raised - https://github.com/protegeproject/protege/pull/1025
* Selection model and entity focus is very buggy
  * clicking/finding an entity frequently does not navigate to that entity
  * sometimes the focus changes when adding relations/subclass axioms

### Protege Features

#### DL Query Tab

For off the cuff queries as simple as `year value -22`


#### Individual Hierarchy Tab

`after`
  * Select `hide orphans`
  * Navigate the `after` tree with `inverse property`
  * ideally use `during` for children and `before` for sibling order

`memberOf`

`location`
  * Temporarily remove `Transitive(location)` and navigate through from `Galaxy`
    * ships make this messy

Issues   
* Requires the reasoner - would be nice to also see asserted tree
    * eg transitivity makes the tree messy with individuals showing at all levels
* lots of duplicate nodes in top level of tree - bug?
* ideal if the nodes worked for selection, but they don't
* we'd require a more advanced tree ideally that allows ordering of siblings by a property (eg after)
    
#### OWLDoc

Export HTML rendering of the ontology for browsing (read only/asserted only).
Seems bugged now.
* html frames not working (in Chrome at least)
* entity pages contain lots of assertions about other entities!!?!?

## Ontology Browser

HTML rendering of an ontology can be run locally using the [ontology browser](https://github.com/nickdrummond/ontology-browser)

### Use

* Get the browser - `https://github.com/co-ode-owl-plugins/ontology-browser`
* Start Mongo - `C:\Program Files\MongoDB\Server\4.4\bin`
* Start ontology browser - `mvn jetty:run`
* Navigate to `localhost:8080`
* Point it at local file: `file:///C:/Users/nickd/Documents/starwarsontology/ontologies/all.owl.ttl`

### Thoughts
* Nice to have cloud views - eg [instance usage](instances-usage-cloud.pdf)
* Can we add axioms (at least to the in-memory ont)?
* Can we then export/save changes?

### Bugs
* see ont browser

## WebProtege

https://webprotege.stanford.edu/

More limited (eg cannot use a reasoner?)
But can import multiple ontologies and looks to have good multi-user support now.

https://protegewiki.stanford.edu/wiki/WebProtegeUsersGuide#Viewing_and_Editing_OWL_2_Ontologies
