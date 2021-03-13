package org.unidue.ub.unidue.almaregister.client;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.unidue.ub.unidue.almaregister.model.Overdue;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;

@Service
public class AlmaAnalyticsReportClient {

    private final static Logger log = LoggerFactory.getLogger(AlmaAnalyticsReportClient.class);

    /**
     * The general path for all Alma analytics reports
     */
    private final static String urlTemplate = "https://api-eu.hosted.exlibrisgroup.com/almaws/v1/analytics/reports?path=%s&apikey=%s";

    @Value("${alma.api.user.key}")
    private String almaUserApiKey;

    public <T> T getReport(String reportPath, Class<T> clazz) throws IOException {
        String url = String.format(urlTemplate, reportPath, almaUserApiKey);
        log.debug("querying url: " + url);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.debug("queried alma api with response: " + response);
        InputStream xslFile = new ClassPathResource("/xslt/analytics2xml.xsl").getInputStream();
        String transformed = transformXmlDocument(response, xslFile);
        log.debug("converted response into string: " + transformed);
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(transformed, clazz);
    }


    /**
     * helper function for the xsl transformation
     * @param inputXmlString the String to be transformed
     * @param xsltFile the filename relativ to the resources folder
     * @return the transformed xml as String
     */
    private String transformXmlDocument(String inputXmlString,
                                        InputStream xsltFile) {

        TransformerFactory factory = TransformerFactory.newInstance();
        StreamSource xslt = new StreamSource(xsltFile);

        StreamSource text = new StreamSource(new StringReader(inputXmlString));
        StringWriter writer = new StringWriter();
        StreamResult textOP = new StreamResult(writer);

        try {
            Transformer transformer = factory.newTransformer(xslt);
            transformer.setParameter("apikey", this.almaUserApiKey);
            transformer.transform(text, textOP);
        } catch (TransformerException e) {
            log.error("could not transform analytics report", e);
        }
        return writer.toString();
    }


}
