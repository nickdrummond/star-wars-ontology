# Ontology Browser Findings

## Use

* Start Mongo - C:\Program Files\MongoDB\Server\4.4\bin
* Start ontology browser - jetty:run
* localhost:8080
* Point it at local file: file:///C:/Users/nickd/Documents/starwarsontology/ontologies/all.owl.ttl

## Thoughts
* re-enable links to clouds - eg clouds/individuals
* Try to re-enable reasoner
* Can we add axioms (at least to the in-memory ont)?
* Can we then export/save changes?
* If sidebar is hidden, no way to get to subclasses

## Bugs
* rendering of hyphens - truncated names eg D'Qar
  Also cannot be searched for
* Scrollbars in left navigation 
  (removed but how about mobile?)
* Rendering of numbers - eg 501st
* No inference when JFact selected (Rebel Scum)
  http://127.0.0.1:8080/?label=c0154cd07d7b26a39ea1e11b7ce220f7_588a8713faa332a36257fe98fb6efe04&redirect=/classes/-738385071/
