FROM nickdrummond/ontology-browser:v4.0.0
COPY ./ontologies /ontologies
ENV ONTOLOGY_ROOT_LOCATION="file:///ontologies/all.owl.ttl"
ENV REASONING_ROOT_IRI="https://nickdrummond.github.io/star-wars-ontology/ontologies/events.owl.ttl"
ENV LABEL_IRI="https://nickdrummond.github.io/star-wars-ontology/util#editorLabel"
ENV PROJECT_NAME="Star Wars Ontology"
ENV PROJECT_TAGLINE="Project Convor - building Star Wars in OWL"
ENV PROJECT_URL="https://nickdrummond.github.io/star-wars-ontology/"
ENV PROJECT_CONTACT="nickdrummond@yahoo.com"
