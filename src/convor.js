javascript: (() => {
    var currentURL = window.location.href;

    var fragment = currentURL.indexOf("#");
    var page = encodeURIComponent((fragment > -1) ? currentURL.substr(0, fragment) : currentURL);
    var propURL = encodeURIComponent("http://www.w3.org/2000/01/seeAlso");

    var searchOntUrl = "http://star-wars-ontology.up.railway.app/entities/annotation?property=seeAlso&search=" + page;

    window.open(searchOntUrl, '_blank');
})();