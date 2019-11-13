package europeana.eu.cclod.edmconverter.service;

import europeana.eu.cclod.edmconverter.properties.CrawlerProperties;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.rdf.model.impl.StatementImpl;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class LinkedDataConversionService {

    private Logger logger = LoggerFactory.getLogger(LinkedDataConversionService.class);

    private CrawlerProperties properties;

    public LinkedDataConversionService(CrawlerProperties properties) {
        this.properties = properties;
    }

    public void convertRdfToEdm() throws IOException {
        Map<String, String> mapping = retrieveMapping();
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(properties.getCrawlerFile().toString());
        model.read(in, null, RDFLanguages.strLangNTriples);
        StmtIterator iterator = model.listStatements();

        Model newModel = ModelFactory.createDefaultModel();
        while (iterator.hasNext()) {
            Statement statement = iterator.next();
            mapStatement(mapping, model, newModel, statement);
            newModel.add(new StatementImpl(statement.getSubject(), RDF.type, new ResourceImpl("http://www.europeana.eu/schemas/edm/ProvidedCHO")));
            logger.debug("Processing statement: {}", statement.toString());
        }
        settingPrefixes(newModel);
        logger.info("Total size of new model: {}", newModel.size());
        newModel.write(new BufferedOutputStream(new FileOutputStream(properties.getOutputFile())), properties.getOutputFormat());
        in.close();
    }

    private void mapStatement(Map<String, String> mapping, Model model, Model newModel, Statement statement) {
        if (mapping.containsKey(statement.getPredicate().toString())) {
            if (statement.getPredicate().toString().contains("width")) {
                newModel.add(new StatementImpl(statement.getSubject(), new PropertyImpl(mapping.get(statement.getPredicate().toString())), model.createLiteral("width: " + statement.getObject())));
            } else if (statement.getPredicate().toString().contains("height")) {
                newModel.add(new StatementImpl(statement.getSubject(), new PropertyImpl(mapping.get(statement.getPredicate().toString())), model.createLiteral("height: " + statement.getObject())));
            } else if (statement.getObject().isLiteral()) {
                Literal liter = (Literal) statement.getObject();
                if (liter.getLanguage().isEmpty()) {
                    newModel.add(new StatementImpl(statement.getSubject(), new PropertyImpl(mapping.get(statement.getPredicate().toString())), model.createLiteral(liter.getString(), properties.getDefaultLanguage())));
                } else {
                    newModel.add(new StatementImpl(statement.getSubject(), new PropertyImpl(mapping.get(statement.getPredicate().toString())), statement.getObject()));
                }
            } else {
                newModel.add(new StatementImpl(statement.getSubject(), new PropertyImpl(mapping.get(statement.getPredicate().toString())), statement.getObject()));
            }
        }
    }

    private void settingPrefixes(Model newModel) {
        newModel.setNsPrefix("db", "http://data.bibliotheken.nl/id/gvn/");
        newModel.setNsPrefix("dc", "http://purl.org/dc/elements/1.1/");
        newModel.setNsPrefix("dcterms", "http://purl.org/dc/terms/");
        newModel.setNsPrefix("edm", "http://www.europeana.eu/schemas/edm/");
    }

    private Map<String, String> retrieveMapping() {
        Map<String, String> mapping = new HashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get(properties.getMappingFile().toString()), StandardCharsets.UTF_8)) {
            stream.forEach(s -> parseMapping(s, mapping));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }

    private void parseMapping(String s, Map<String, String> map) {
        String[] splitString = s.split("=");
        map.put(splitString[0], splitString[1]);
    }
}
