package europeana.eu.cclod.edmconverter.component;

import europeana.eu.cclod.edmconverter.properties.CrawlerProperties;
import europeana.eu.cclod.edmconverter.service.LinkedDataConversionService;
import europeana.eu.cclod.edmconverter.service.LinkedDataCrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerComponent implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(CommandLineRunner.class);

    private LinkedDataCrawlerService crawlerService;

    private LinkedDataConversionService conversionService;

    private CrawlerProperties properties;

    public CommandLineRunnerComponent(LinkedDataCrawlerService crawlerService, LinkedDataConversionService conversionService, CrawlerProperties properties) {
        this.crawlerService = crawlerService;
        this.conversionService = conversionService;
        this.properties = properties;
    }

    @Override
    public void run(String... args) throws Exception {
            logger.info("Starting processing for url: {}", properties.getDsUri());
            crawlerService.crawl();
            logger.info("Crawling finished, starting conversion to: {}", properties.getOutputFile());
            conversionService.convertRdfToEdm();
    }
}
