package laterooms;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by abraithwaite on 24/04/2014.
 */
@Component
public class Restrictions extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        XmlJsonDataFormat xmlJsonDataFormat = new XmlJsonDataFormat();
        xmlJsonDataFormat.setSkipNamespaces(true);

        from("file:files/Restrictions")
                .convertBodyTo(String.class)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String body = (String) exchange.getIn().getBody();
                        String filename = exchange.getIn().getHeader("CamelFileName", String.class);
                        String newFilename = filename.replace(".xml", ".json");

                        String newBody = body.substring(1);
                        exchange.getIn().setBody(newBody);
                        exchange.getIn().setHeader("CamelFileName", newFilename);
                    }
                })
                .marshal(xmlJsonDataFormat)
                .to("file:output/Restrictions");
    }
}
