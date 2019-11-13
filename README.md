# Europeana CC LOD EDM converter

This application is made to convert RDF from one format to EDM. It is made as an experiment for the Royal Library dataset.

### How to run the application
A sample version of the application can be build and run with the following command:

`docker-compose build`
`docker-compose up`

This will use the centsprenten database from the Royal Library as an example.
The output it produces will be stored in the data folder.
When needed the docker-compose file can be changed to switch the volume mapping.

### Properties
The applicatie has the following properties which van be set through the environment in the docker-compose file

#### 
`converter.dsUri` This properties is used to indicate the source URL from which the data needs te be crawled
`converter.outputFile` Necessary. This indicates the outputFile to which the data needs to be written
`converter.mappingFile` Necessary. This indicates the mappingFile which is used in the mapping of the crawled format to the EDM format
`converter.crawlerFile` Necessary. This indicates the location of the crawled file, outputted by the crawler. This file is only needed to check if the crawling went correctly.
`converter.defaultLanguage` Default=`nl` This is the default language which will be se to all literals missing a language. Only need to set when default is incorrect.
`converter.outputFormat` Default=`TTL` This indicates the output format of the file. Only need to be set when default is incorrect. Options are: "RDF/XML", "RDF/XML-ABBREV", "N-TRIPLE", "TURTLE", (and "TTL") and "N3"

### Mapping
The mapping file (/data/mapping/mapping.txt) indicates which predicates need to be converted.
In the example we indicate that the `http://schema.org/author` need to be converted to `http://purl.org/dc/elements/1.1/creator`.
This meand that from the original crawled output we go from:
`<http://data.bibliotheken.nl/id/gvn/KONB14Borms0657> <http://schema.org/author> "Dirk van Lubeek (1747-1811)" .`

To:
`<dc:creator xml:lang="nl">Dirk van Lubeek (1747-1811)</dc:creator>`

Currently there are two special mappings:
- Height and Width are both mapped to extend which means they need to be prefixed.
- Thumbnails need to be reformatted, when encountered it will edit the link to the thumbnail