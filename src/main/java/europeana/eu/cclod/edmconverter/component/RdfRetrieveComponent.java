package europeana.eu.cclod.edmconverter.component;

import europeana.eu.cclod.edmconverter.properties.CrawlerProperties;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFReader;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

@Component
public class RdfRetrieveComponent {

    private Logger logger = LoggerFactory.getLogger(RdfRetrieveComponent.class);

    private static final String CONTENT_TYPES_ACCEPT_HEADER = Lang.RDFXML.getContentType().getContentType() + ", " + Lang.TURTLE.getContentType().getContentType() + ", " + Lang.JSONLD.getContentType().getContentType();

    private CrawlerProperties properties;

    public RdfRetrieveComponent(CrawlerProperties properties) {
        this.properties = properties;
    }

    public Model readRdfFromUri() throws IOException {
        HttpURLConnection con = (HttpURLConnection) properties.getDsUri().openConnection();
        con.setRequestProperty("Accept", CONTENT_TYPES_ACCEPT_HEADER);
        con.setRequestMethod("GET");
        logger.info("Received response code: {}", con.getResponseCode());
        Lang contentType = fromMimeType(con.getHeaderField("Content-Type"));
        InputStream inputStream = con.getInputStream();

		return readRdf(inputStream, contentType);
    }

    private Lang fromMimeType(String mimeType) {
        if(mimeType==null)
            return null;
        if (mimeType.contains(";"))
            mimeType=mimeType.substring(0, mimeType.indexOf(';')).trim();
        if (mimeType.contains(","))
            mimeType=mimeType.substring(0, mimeType.indexOf(',')).trim();
        return RDFLanguages.contentTypeToLang(mimeType);
    }

    private Model readRdf(InputStream content, Lang l) {
        Model model = ModelFactory.createDefaultModel();
        RDFReader reader = model.getReader(l.getName());
        reader.setProperty("allowBadURIs", "true");
        reader.read(model, content, null);
        return model;
    }

}
