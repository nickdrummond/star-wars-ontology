javascript: (() => {
    const currentURL = window.location.href;
    const fragment = currentURL.indexOf("#");
    const page = encodeURIComponent((fragment > -1) ? currentURL.substring(0, fragment) : currentURL);
    const searchOntUrl = "https://www.star-wars-ontology.co.uk/entities/annotation?property=seeAlso&search=" + page;
    window.open(searchOntUrl, '_blank');
})();