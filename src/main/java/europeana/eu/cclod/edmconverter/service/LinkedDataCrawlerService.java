package europeana.eu.cclod.edmconverter.service;

import com.ontologycentral.ldspider.Crawler;
import com.ontologycentral.ldspider.frontier.BasicFrontier;
import com.ontologycentral.ldspider.frontier.Frontier;
import com.ontologycentral.ldspider.hooks.content.ContentHandler;
import com.ontologycentral.ldspider.hooks.content.ContentHandlerNx;
import com.ontologycentral.ldspider.hooks.content.ContentHandlerRdfXml;
import com.ontologycentral.ldspider.hooks.content.ContentHandlers;
import com.ontologycentral.ldspider.hooks.links.LinkFilterDomain;
import com.ontologycentral.ldspider.hooks.sink.Sink;
import com.ontologycentral.ldspider.hooks.sink.SinkCallback;
import com.ontologycentral.ldspider.queue.HashTableRedirects;
import com.ontologycentral.ldspider.seen.HashSetSeen;
import europeana.eu.cclod.edmconverter.properties.CrawlerProperties;
import europeana.eu.cclod.edmconverter.component.RdfRetrieveComponent;
import europeana.eu.cclod.edmconverter.model.CallbackTriplesQuadsBufferedWriter;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LinkedDataCrawlerService {

    private Logger logger = LoggerFactory.getLogger(LinkedDataCrawlerService.class);

    private CrawlerProperties properties;

    private RdfRetrieveComponent rdfRetrieveComponent;

    public LinkedDataCrawlerService(CrawlerProperties properties, RdfRetrieveComponent rdfRetrieveComponent) {
        this.properties = properties;
        this.rdfRetrieveComponent = rdfRetrieveComponent;
    }

    public void crawl() throws URISyntaxException, IOException {
        logger.info("Starting to crawl");
        Frontier frontier = new BasicFrontier();
        int totalSeeds = 0;
        totalSeeds = gatherUris(frontier, totalSeeds);

        Crawler crawler = new Crawler(5);
        ContentHandler contentHandler = new ContentHandlers(new ContentHandlerRdfXml(), new ContentHandlerNx());
        crawler.setContentHandler(contentHandler);

        FileWriter out = new FileWriter(properties.getCrawlerFile());
        BufferedWriter out2 = new BufferedWriter(out);
        CallbackTriplesQuadsBufferedWriter writer = new CallbackTriplesQuadsBufferedWriter(out2, false, Lang.NTRIPLES);
        Sink sink = new SinkCallback(writer, false);

        crawler.setOutputCallback(sink);
        LinkFilterDomain linkFilter = new LinkFilterDomain(frontier);

        crawler.setLinkFilter(linkFilter);

        crawler.evaluateBreadthFirst(frontier, new HashSetSeen(), new HashTableRedirects(), totalSeeds, 0, -1, -1,
                false);
        out2.flush();
        out.close();
        crawler.close();
    }

    private int gatherUris(Frontier frontier, int totalSeeds) throws IOException, URISyntaxException {
        Model model = rdfRetrieveComponent.readRdfFromUri();
        for (String uri : listRootResources(model)) {
            totalSeeds++;
            frontier.add(new URI(uri));
        }
        return totalSeeds;
    }

    private List<String> listRootResources(Model model) {
        ArrayList<String> uris = new ArrayList<>();
        Resource dsResource = model.createResource(properties.getDsUri().toString());
        if (dsResource == null) return uris;
        StmtIterator voidRootResources = dsResource.listProperties(ResourceFactory.createProperty("http://rdfs.org/ns/void#rootResource"));
        voidRootResources.forEachRemaining(st -> uris.add(st.getObject().asResource().getURI()));
        return uris;
    }

}
