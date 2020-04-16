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

@Component
public class PagePreparator {

    private static final Logger log = LoggerFactory.getLogger(PagePreparator.class);

    @Value("${alma.register.master.template:https://www.uni-due.de}")
    private String masterTemplateHost;

    @Value("${alma.register.master.template:/ub}")
    private String masterTemplatePath;

    @Value("${alma.register.datadir:#{systemProperties['user.home']}/.almaregister/}")
    private String localTemplateFolder;

    @Scheduled(cron = "0 0 2 * * *")
    //@Scheduled(fixedRate = 5000)
    public void collectMasterTemplate() {
        try {
            Document doc = Jsoup.connect(masterTemplateHost + masterTemplatePath).get();
            Elements html = doc.select("html");
            html.attr("xmlns:th", "http://www.thymeleaf.org");
            Elements content = doc.select("#content__standard__main");
            doc.select("script[type='text/x-mathjax-config']").remove();
            content.attr("layout:fragment", "content");
            content.html("");
            setAbsolutePaths(doc, "img", "src");
            setAbsolutePaths(doc, "link", "href");
            setAbsolutePaths(doc, "a", "href");
            setAbsolutePaths(doc, "script", "src");
            setAbsolutePaths(doc, "meta", "content");
            File f = new File(localTemplateFolder,"layout.html");
            FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
            log.info("updated web template");
        } catch (IOException ioe) {
            log.warn("could not update page.", ioe);
        }

    }

    private void setAbsolutePaths(Document doc, String tag, String attribute) {
        Elements elements = doc.select(tag + "[" + attribute + "]");
        for (Element src : elements) {
            String path = src.attr(attribute);
            src.attr(attribute,masterTemplateHost + path);
        }
    }
}
