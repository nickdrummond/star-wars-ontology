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

None of these queries is complete using just the basic graph.

We use the jena SPARQL engine as it:
* allows transitive +/* properties (not supported in Protege SPARQL Tab)
  [Transitive SPARQL 1.1](https://stackoverflow.com/questions/8569810/sparql-querying-transitive)
* can use the RDFS infmodel, using inference to make results (more) complete.
  See [Jena inference](https://jena.apache.org/documentation/inference/)


## Resources
[SPARQL by example](https://www.w3.org/2009/Talks/0615-qbe/)

## Queries

### Death year
Someone who was killed during an event died on the date of that event.

eg **Plo Koon died during Order 66 on 19BBY**

We cannot simply do this in OWL as there are no property chains for datatype properties.

See [deaths.sparql](deaths.sparql) and [Run in browser](https://www.star-wars-ontology.co.uk/sparql?prefixes=PREFIX+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0D%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0D%0APREFIX+swrlb%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2003%2F11%2Fswrlb%23%3E%0D%0APREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3E%0D%0APREFIX+%3A+%3Chttps%3A%2F%2Fnickdrummond.github.io%2Fstar-wars-ontology%2Fontologies%23%3E%0D%0APREFIX+dc%3A+%3Chttp%3A%2F%2Fpurl.org%2Fdc%2Felements%2F1.1%2F%3E%0D%0APREFIX+dct%3A+%3Chttp%3A%2F%2Fpurl.org%2Fdc%2Fterms%2F%3E%0D%0APREFIX+xml%3A+%3Chttp%3A%2F%2Fwww.w3.org%2FXML%2F1998%2Fnamespace%3E%0D%0APREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E&select=SELECT+DISTINCT+%3Fdied%0D%0A+++++++%3Fevent%0D%0A+++++++%3Floc%0D%0A+++++++%3Fperson%0D%0A+++++++%3Fage%0D%0AWHERE+%7B%0D%0A%09++%3Fperson+a+%3ALiving_thing+.%0D%0A+++%09++%3Fevent+rdf%3Atype+%3ADeath+%3B%0D%0A+++++++++++++%3Aof+%3Fperson+%3B%0D%0A+++++++++++++%3Aduring*%2F%3Ayear+%3Fdied+.%0D%0A++++++OPTIONAL+%7B+%3Fevent+rdf%3Atype+%3ADeath+%3B%0D%0A++++++++++++++++++++++++%3Aof+%3Fperson+%3B%0D%0A+++%09++++++++++++++++++++%3Aat+%3Fdirectlocation+%3B%0D%0A+++++++++++++++++++++++++%7D+.%0D%0A++++++OPTIONAL+%7B+%3Fevent+rdf%3Atype+%3ADeath+%3B%0D%0A++++++++++++++++++++++++%3Aof+%3Fperson+%3B%0D%0A+++%09++++++++++++++++++++%3Aduring%2F%3AlocatedIn+%3Flocation1Step+.%0D%0A+++++++++++++++++++++++++%7D+.%0D%0A++++++OPTIONAL+%7B+%3Fevent+rdf%3Atype+%3ADeath+%3B%0D%0A++++++++++++++++++++++++%3Aof+%3Fperson+%3B%0D%0A+++%09++++++++++++++++++++%3Aduring%2F%3Aduring%2F%3AlocatedIn+%3Flocation2Step+.+%23+never+more+than+2+steps+from+a+%22full+Event%22%0D%0A+++++++++++++++++++++++++%7D+.%0D%0A%09++OPTIONAL+%7B+%3Fe1+rdf%3Atype+%3ABirth+%3B%0D%0A%09++++++++++++++++++++%3Aof+%3Fperson+%3B%0D%0A%09++++++++++++++++++++%3Ayear+%3Fborn+.%0D%0A%09++++++++++++++++++++%7D+.%0D%0A++++++BIND+%28%3Fdied+-+%3Fborn+AS+%3Fage%29+.%0D%0A++++++BIND+%28%28IF+%28bound%28%3Fdirectlocation%29%2C+%3Fdirectlocation%2C+IF+%28bound%28%3Flocation1Step%29%2C+%3Flocation1Step%2C+%3Flocation2Step%29%29%29+AS+%3Floc%29+.%0D%0A%7D%0D%0AORDER+BY+%3Fdied+%3FdiedDuring+%3Floc+%3Fname&pageSize=20)

### Events with location

See [events.sparql](events.sparql) and [Run in browser](https://www.star-wars-ontology.co.uk/sparql?prefixes=PREFIX+owl%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0D%0APREFIX+rdf%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0D%0APREFIX+swrlb%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2003%2F11%2Fswrlb%23%3E%0D%0APREFIX+xsd%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3E%0D%0APREFIX+%3A+%3Chttps%3A%2F%2Fnickdrummond.github.io%2Fstar-wars-ontology%2Fontologies%23%3E%0D%0APREFIX+dc%3A+%3Chttp%3A%2F%2Fpurl.org%2Fdc%2Felements%2F1.1%2F%3E%0D%0APREFIX+dct%3A+%3Chttp%3A%2F%2Fpurl.org%2Fdc%2Fterms%2F%3E%0D%0APREFIX+xml%3A+%3Chttp%3A%2F%2Fwww.w3.org%2FXML%2F1998%2Fnamespace%3E%0D%0APREFIX+rdfs%3A+%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E&select=SELECT+DISTINCT+%3Fyear+%3Flocation+%3Fevent+%3Fduring+%3Fafter%0D%0AWHERE+%7B%0D%0A%09++%3Fevent+a+%3AEvent+.%0D%0A%09++OPTIONAL+%7B+%3Fevent+%3Aat+%3Flocation+%7D+.%0D%0A%09++OPTIONAL+%7B+%3Fevent+%3Ayear+%3Fyear+%7D+.%0D%0A%09++OPTIONAL+%7B+%3Fevent+%3Aduring+%3Fduring+%7D+.%0D%0A%09++OPTIONAL+%7B+%3Fevent+%3Aafter+%3Fafter+%7D+.%0D%0A%09++%7D%0D%0AORDER+BY+ASC+%28+%3Fyear+%29+%28+%3Flocation+%29+%28%3Fduring%29)


#### References

* [SWRL language FAQ](https://github.com/protegeproject/swrlapi/wiki/SWRLLanguageFAQ)
* [SWRL tutorial](https://drive.google.com/file/d/1Ofk0HxmJdKspsSAmzOdB9PsXpvR3nJqv/view)
* [Protege SWQRL support](https://github.com/protegeproject/swrlapi/wiki/SQWRL)