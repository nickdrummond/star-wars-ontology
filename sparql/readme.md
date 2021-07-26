Someone who was killed during an event died on the date of that event
- need to infer "died" from event
- eg Plo Koon died in Order 66 on 19BBY

Straight OWL
------------
You can't use property chains for datatype properties

SWRL
--------------------
https://github.com/protegeproject/swrlapi/wiki/SWRLLanguageFAQ
https://drive.google.com/file/d/1Ofk0HxmJdKspsSAmzOdB9PsXpvR3nJqv/view

Cannot be used in reasonable time
 
Sentient(?person) ^ diedIn(?person, ?event) ^ year(?event, ?y) -> diedInYear(?person, ?y)
SWRL tab freezes up when we try to run it
Reduced the number of instances to just Attack_on_the_Raddus + Leia + Ackbar
It runs the rule pretty much instantly but you have to assert them back into the ontology with the SWRL tab - clunky

If we just want to query
https://github.com/protegeproject/swrlapi/wiki/SQWRL
Sentient(?person) ^ diedIn(?person, ?event) ^ year(?event, ?y) -> sqwrl:select(?person, ?y) ^ sqwrl:orderBy(?y)

This works fine again in the simple case, but looks like Drools can't handle the number of individuals

SPARQL
------
Sparql works fine using an RDFS Inference model - see people.sparql and SparqlTest

Neither of these queries is complete using just the basic graph.
We need to find an engine that allows the transitive + (not supported in Protege SPARQL Tab)
(https://stackoverflow.com/questions/8569810/sparql-querying-transitive)

This is possible in Jena. This is not the job of the SPARQL engine, its attaching a reasoner to the model:
https://jena.apache.org/documentation/inference/

SPARQL resources:
https://www.w3.org/2009/Talks/0615-qbe/
