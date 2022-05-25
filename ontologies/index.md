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

<div id="index">

</div>

<script>
  (async () => {
    const response = await fetch('https://api.github.com/repos/nickdrummond/star-wars-ontology/contents/ontologies/');
    const data = await response.json();
    let htmlString = '<ul>';
    
    for (let file of data) {
      if (file.name.endsWith(".ttl")) {
        htmlString += `<li><a href="${file.name}">${file.name}</a> (${file.size/1000.0}k)</li>`;
      }
    }

    htmlString += '</ul>';
    document.getElementById('index').innerHTML = htmlString;
  })()
</script>