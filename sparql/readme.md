[home](../) |
[docs](../docs/) |
[benefits](../docs/benefits.md) |
[modules](../docs/modularisation.md) |
[events](../docs/events.md) |
[modelling principles](../docs/modelling-principles.md) |
[test questions](../docs/test-questions.md) |
[performance](../docs/performance.md) |
[tools](../docs/tools.md)

# SPARQL

## Resources
[SPARQL by example](https://www.w3.org/2009/Talks/0615-qbe/)

## Querying for death year
Someone who was killed during an event died on the date of that event.

eg **Plo Koon died during Order 66 on 19BBY**

We cannot simply do this in OWL as there are no property chains for datatype properties.

See [deaths.sparql](deaths.sparql) and [DeathsSparqlReport](../src/test/java/com/nickd/sw/report/DeathsSparqlReport.java)

Neither of these queries is complete using just the basic graph.

We use the jena SPARQL engine as it:
* allows transitive +/* properties (not supported in Protege SPARQL Tab)
[Transitive SPARQL 1.1](https://stackoverflow.com/questions/8569810/sparql-querying-transitive)
* can use the RDFS infmodel, using inference to make results (more) complete.
See [Jena inference](https://jena.apache.org/documentation/inference/)

### SWRL alternative

Cannot be used in reasonable time as the SWRL tab freezes up when we try to run it

    Sentient(?person) ^ diedIn(?person, ?event) ^ year(?event, ?y) -> diedInYear(?person, ?y)

We tried reducing the number of instances to just Attack_on_the_Raddus + Leia + Ackbar
It runs the rule pretty much instantly but you have to assert them back into the ontology with the SWRL tab - clunky

If we just want to query, we can try SWQRL

    Sentient(?person) ^ diedIn(?person, ?event) ^ year(?event, ?y) -> sqwrl:select(?person, ?y) ^ sqwrl:orderBy(?y)

This works fine again in the simple case, but looks like Drools can't handle the number of individuals

#### References

* [SWRL language FAQ](https://github.com/protegeproject/swrlapi/wiki/SWRLLanguageFAQ)
* [SWRL tutorial](https://drive.google.com/file/d/1Ofk0HxmJdKspsSAmzOdB9PsXpvR3nJqv/view)
* [Protege SWQRL support](https://github.com/protegeproject/swrlapi/wiki/SQWRL)