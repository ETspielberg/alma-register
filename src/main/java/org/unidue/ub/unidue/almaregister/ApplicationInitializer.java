package org.unidue.ub.unidue.almaregister;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationInitializer {

    @Value("${alma.register.datadir:#{systemProperties['user.home']}/.almaregister/}")
    private String localTemplateFolder;

    @Value("classpath:templates/")
    private String generalTemplateFolder;

    private final static Logger log = LoggerFactory.getLogger(ApplicationInitializer.class);

    private final PagePreparator pagePreparator;

    ApplicationInitializer(PagePreparator pagePreparator) {
        this.pagePreparator = pagePreparator;
    }

    @PostConstruct
    private void init() {
        log.info("AppInitializator initialization template locations");
        File directory = new File(localTemplateFolder);
        if (! directory.exists())
            directory.mkdirs();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            try {
                Resource[] resources = resolver.getResources("classpath*:templates/*.html");
                log.debug("found " + resources.length + " pages to copy to local folder");
                for (Resource resource : resources) {
                    File input = resource.getFile();
                    String filename = input.getName();

                    File output = new File(localTemplateFolder + filename);
                    log.debug("copying file " + filename + " from path " + input.getPath() + " to " + output.getPath());
                    try {
                        Files.copy(input.toPath(), output.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (FileAlreadyExistsException faee) {
                        log.info("file " + filename + " already exists in local template folder");
                    }
                }
            } catch (Exception e) {
                log.error("could not copy files", e);
            }
            this.pagePreparator.collectMasterTemplate();

    }
}
