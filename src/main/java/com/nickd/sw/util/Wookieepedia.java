package com.nickd.sw.util;

import com.nickd.sw.builder.Constants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Wookieepedia {

    private final Logger logger = LoggerFactory.getLogger(Wookieepedia.class);

//TODO cache    private final static Map<URI, WookiepediaHelper>

    private final Helper helper;
    private final OWLAnnotationProperty seeAlso;
    private Document doc;

    private LinkedHashMap<OWLEntity, String> knownEntities;
    private LinkedHashMap<OWLEntity, String> unknownEntities;


    public static Wookieepedia forURI(Helper helper, URI uri) throws IOException {
        return new Wookieepedia(helper, uri);
    }

    public static Wookieepedia forOWLEntity(Helper helper, OWLEntity entity) throws IOException {
        return new Wookieepedia(helper, entity);
    }

    public static Wookieepedia forName(Helper helper, String ref) throws IOException {
        return forURI(helper, URI.create(Constants.WOOKIEEPEDIA_BASE + ref));
    }

    private Wookieepedia(Helper helper, URI uri) throws IOException {
        this.helper = helper;
        this.seeAlso = helper.df.getRDFSSeeAlso();
//        System.setProperty("javax.net.ssl.trustStore", "certs/_.fandom.com.jks");
        load(uri);
    }

    private Wookieepedia(Helper helper, OWLEntity entity) throws IOException {
        this.helper = helper;
        this.seeAlso = helper.df.getRDFSSeeAlso();
        load(entity);
    }

    private void load(OWLEntity entity) throws IOException {
        getWookieepediaRefsFor(entity).forEach(iri -> {
            try {
                load(URI.create(iri));
            } catch (IOException e) {
                logger.error("${} seeAlso is not valid URI: ${}", entity, iri);
            }
        });
    }

    private Stream<String> getWookieepediaRefsFor(OWLEntity entity) {
        return helper.ont.annotationAssertionAxioms(entity.getIRI(), Imports.INCLUDED)
                .filter(ax -> ax.getProperty().equals(seeAlso))
                .map(OWLAnnotationAssertionAxiom::getValue)
                .map(OWLAnnotationValue::asLiteral)
                .flatMap(Optional::stream)
                .map(OWLLiteral::getLiteral);
    }

    private void load(URI uri) throws IOException {
        doc = getFromWebOrCache(uri);
        buildLinksIndex(uri);
    }

    private void buildLinksIndex(URI uri) {

        String path = uri.getPath();
        String base = path.substring(0, path.lastIndexOf("/"));
        String root = uri.getScheme() + "://" + uri.getAuthority();

        suggestType(doc).forEach(t -> System.out.println(helper.render(t)));

        Elements links = new Elements();
        links.addAll(doc.select("aside.portable-infobox a"));
        links.addAll(doc.select("#mw-content-text p a"));

        // Retain order of the links for more pertinent suggestions
        knownEntities = new LinkedHashMap<>();
         unknownEntities = new LinkedHashMap<>();

        links.stream()
                .map(l -> l.attr("href"))
                .distinct()
                .filter(h -> h.startsWith(base))
                .map(h -> (h.startsWith("/") ? root + h : h))
                .forEach(href -> {
                    List<OWLEntity> matches = FinderUtils.annotationExact(href, seeAlso, helper);
                    if (matches.isEmpty()) {
                        unknownEntities.put(helper.ind(wikiPageName(URI.create(href))), href);
                    } else {
                        matches.forEach(e -> knownEntities.put(e, href));
                    }
                });
    }

    // Will only work with species
    private Set<OWLClass> suggestType(Document doc) {
        Set<String> likelyTypes = Set.of("Species");

        Elements data = doc.select("aside.portable-infobox .pi-data");

        for (Element d : data) {
            String property = d.select(".pi-data-label").first().text();
            Element value = d.select(".pi-data-value").first();
//            String valueString = value.text();
            String valueLink = value.select("a[href]").first().text();
            if (likelyTypes.contains(property)) {
                return FinderUtils.annotationExact(valueLink, seeAlso, helper).stream()
                        .filter(OWLEntity::isOWLClass)
                        .map(AsOWLClass::asOWLClass)
                        .collect(Collectors.toSet());

            }
        }
        return Collections.emptySet();
    }

    private Document getFromWebOrCache(URI uri) throws IOException {
        File cacheFile = cacheFileFor(uri);

        if (!cacheFile.exists()) {
            CurlUtils.curl(uri, cacheFile);
        }
        return Jsoup.parse(cacheFile);
    }

    private File cacheFileFor(URI uri) {
        return new File(Constants.CACHES + wikiPageName(uri) + ".html");
    }

    private String wikiPageName(URI uri) {
        String path = uri.getPath();
        return path.substring(path.lastIndexOf("/"));
    }

    public List<OWLEntity> getKnown() {
        return new ArrayList<>(knownEntities.keySet());
    }

    public List<OWLEntity> getUnknown() {
        return new ArrayList<>(unknownEntities.keySet());
    }
}
