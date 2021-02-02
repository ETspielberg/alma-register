package org.unidue.ub.unidue.almaregister.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.unidue.ub.unidue.almaregister.model.OverdueReport;
import org.unidue.ub.unidue.almaregister.model.Results;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;

@Service
public class AlmaAnalyticsReportClient {

    private final Logger log = LoggerFactory.getLogger(AlmaAnalyticsReportClient.class);

    @Value("${alma.api.user.key}")
    private String almaUserApiKey;

    public OverdueReport[] getReport() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api-eu.hosted.exlibrisgroup.com/almaws/v1/analytics/reports?path=/shared/Universität+Duisburg-Essen+49HBZ_UDE/Reports/Benutzer+nach+3.+Mahnung&apikey=" + almaUserApiKey;
        String response = restTemplate.getForObject(url, String.class);
        File xslFile = new ClassPathResource("/xsl/OverdueReport.xsl").getFile();
        String transformed = transformXmlDocument(response, xslFile);
        log.info(transformed);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,ACCEPT_SINGLE_VALUE_AS_ARRAY )
                .disable(FAIL_ON_UNKNOWN_PROPERTIES, FAIL_ON_IGNORED_PROPERTIES);
        return xmlMapper.readValue(transformed, OverdueReport[].class);
    }

    public static String transformXmlDocument(String inputXmlString,
                                              File xsltFile) {

        TransformerFactory factory = TransformerFactory.newInstance();
        StreamSource xslt = new StreamSource(xsltFile);

        StreamSource text = new StreamSource(new StringReader(inputXmlString));
        StringWriter writer = new StringWriter();
        StreamResult textOP = new StreamResult(writer);

        try {
            Transformer transformer = factory.newTransformer(xslt);
            transformer.transform(text, textOP);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }


}
