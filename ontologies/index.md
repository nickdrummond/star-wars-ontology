[home](../) |
[docs](../docs/) |
[benefits](../docs/benefits.md) |
[modules](../docs/modularisation.md) |
[events](../docs/events.md) |
[modelling principles](../docs/modelling-principles.md) |
[test questions](../docs/test-questions.md) |
[performance](../docs/performance.md) |
[tools](../docs/tools.md)

# Ontologies

See [modules](../docs/modularisation.md) for a description of each of the ontologies.

Import or load directly from this directory - eg:

    https://nickdrummond.github.io/star-wars-ontology/ontologies/all.owl.ttl

## Contents

{% assign doclist = site.pages | sort: 'url'  %}
<ul>
{% for doc in doclist %}
{% if doc.name contains '.owl' %}
<li><a href="{{ site.baseurl }}{{ doc.url }}">{{ doc.url }}</a></li>
{% endif %}
{% endfor %}
</ul>