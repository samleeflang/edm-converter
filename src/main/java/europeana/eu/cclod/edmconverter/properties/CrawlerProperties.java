package europeana.eu.cclod.edmconverter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.net.URL;

@Validated
@ConfigurationProperties("converter")
public class CrawlerProperties {

    @NotNull
    private URL dsUri;

    @NotNull
    private File outputFile;

    @NotNull
    private File mappingFile;

    @NotNull
    private File crawlerFile;

    @NotNull
    private String defaultLanguage;

    @NotNull
    private String outputFormat;

    public URL getDsUri() {
        return dsUri;
    }

    public void setDsUri(URL dsUri) {
        this.dsUri = dsUri;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public File getMappingFile() {
        return mappingFile;
    }

    public void setMappingFile(File mappingFile) {
        this.mappingFile = mappingFile;
    }

    public File getCrawlerFile() {
        return crawlerFile;
    }

    public void setCrawlerFile(File crawlerFile) {
        this.crawlerFile = crawlerFile;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }
}
