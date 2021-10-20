package org.unidue.ub.unidue.almaregister;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class PagePreparator {

    private static final Logger log = LoggerFactory.getLogger(PagePreparator.class);

    @Value("${alma.register.master.template:https://www.uni-due.de}")
    private String masterTemplateHost;

    @Value("${alma.register.master.template.path:/ub}")
    private String masterTemplatePath;

    @Value("${alma.register.master.template.path.en:/ub/en/eindex.php}")
    private String masterTemplatePathEn;

    @Value("${alma.register.datadir:#{systemProperties['user.home']}/.almaregister/}")
    private String localTemplateFolder;

    /**
     * collects the template file from the given web page, cleans it and stores it as master template for the thymeleaf template engine
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void collectMasterTemplate() {
        List<String> languages = Arrays.asList("en", "de");
        for (String language : languages) {
            try {
                String fragment = "layout:fragment";
                Document doc;
                String templateFile;
                if ("en".equals(language)) {
                    doc = Jsoup.connect(masterTemplateHost + masterTemplatePathEn).get();
                    fragment = "layout:fragment";
                    templateFile = "layout_en.html";
                } else {
                    doc = Jsoup.connect(masterTemplateHost + masterTemplatePath).get();
                    templateFile = "layout_de.html";
                }
                Elements html = doc.select("html");
                html.attr("xmlns:th", "http://www.thymeleaf.org");
                Elements content = doc.select("#content__standard__main");
                doc.select("script[type='text/x-mathjax-config']").remove();
                content.attr(fragment, "content");
                content.html("");
                setAbsolutePaths(doc, "img", "src");
                setAbsolutePaths(doc, "link", "href");
                setAbsolutePaths(doc, "a", "href");
                setAbsolutePaths(doc, "script", "src");
                setAbsolutePaths(doc, "meta", "content");
                File f = new File(localTemplateFolder, templateFile);
                FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
                log.info("updated web template for " + language);
            } catch (IOException ioe) {
                log.warn("could not update page for " + language, ioe);
            }
        }
    }

    private void setAbsolutePaths(Document doc, String tag, String attribute) {
        Elements elements = doc.select(tag + "[" + attribute + "]");
        for (Element src : elements) {
            String path = src.attr(attribute);
            if (path.startsWith("/"))
                src.attr(attribute, masterTemplateHost + path);
        }
    }
}
