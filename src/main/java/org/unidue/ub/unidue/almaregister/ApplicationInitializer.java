package org.unidue.ub.unidue.almaregister;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.unidue.ub.unidue.almaregister.service.ScheduledService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * initializes the application. the contained base-templates are copied to the template folder and the master template
 * is collected.
 */
@Component
public class ApplicationInitializer {

    @Value("${alma.register.datadir:#{systemProperties['user.home']}/.almaregister/}")
    private String localTemplateFolder;

    private final static Logger log = LoggerFactory.getLogger(ApplicationInitializer.class);

    private final PagePreparator pagePreparator;

    private final ScheduledService scheduledService;

    /**
     * constructor based autowiring of the page preparator bean
     *
     * @param pagePreparator The page preparator responsible for constructing a themed page template.
     */
    ApplicationInitializer(PagePreparator pagePreparator,
                           ScheduledService scheduledService) {
        this.pagePreparator = pagePreparator;
        this.scheduledService = scheduledService;
    }

    @PostConstruct
    private void init() {
        log.info("AppInitializer initializes template locations");
        File directory = new File(localTemplateFolder);
        if (!directory.exists())
            directory.mkdirs();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("/templates/*.html");
            log.info("found " + resources.length + " pages to copy to local folder");
            for (Resource resource : resources) {
                InputStream input = resource.getInputStream();
                String filename = resource.getFilename();

                File output = new File(localTemplateFolder + filename);
                log.info("copying file " + filename + " from path " + "xx" + " to " + output.getPath());
                try {
                    Files.copy(input, output.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    log.info("copied file " + filename);
                } catch (FileAlreadyExistsException faee) {
                    log.info("file " + filename + " already exists in local template folder");
                }
            }
        } catch (Exception e) {
            log.error("could not copy files", e);
        }
        this.pagePreparator.collectMasterTemplate();
        try {
            this.scheduledService.runImportJob();
        } catch (Exception e) {
            log.warn("could not collect his data.", e);
        }
    }
}
