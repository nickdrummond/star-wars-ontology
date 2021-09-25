# Tools

[back to index](index.md)

## Protege

We used [Protege](https://protege.stanford.edu) to build this ontology

TODO screenshot

### Reasoning

Use the HermiT reasoner

### Nice features
* Spend some time in the Individual Hierarchy Tab - great for membership or temporal event relations
    * Requires the reasoner for inv property tree
    * Remove `Transitive(location)` and navigate through this hierarchy - nice


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