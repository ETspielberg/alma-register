package org.unidue.ub.unidue.almaregister.configuration;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.annotation.PostConstruct;

@Configuration
public class ThymeleafConfiguration {

    @Value("${alma.register.datadir:#{systemProperties['user.home']}/.almaregister/}")
    private String localTemplateFolder;

    private final SpringTemplateEngine springTemplateEngine;

    ThymeleafConfiguration(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    @PostConstruct
    public void extension() {
        FileTemplateResolver localResolver = new FileTemplateResolver();
        localResolver.setPrefix(localTemplateFolder);
        localResolver.setSuffix(".html");
        localResolver.setTemplateMode("HTML5");
        localResolver.setOrder(1);
        localResolver.setOrder(this.springTemplateEngine.getTemplateResolvers().size());
        localResolver.setCacheable(false);
        this.springTemplateEngine.addTemplateResolver(localResolver);
        this.springTemplateEngine.addDialect(new LayoutDialect());
    }

}
