package org.unidue.ub.unidue.almaregister.configuration;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.spring5.webflow.view.AjaxThymeleafViewResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.annotation.PostConstruct;

/**
 * configuration for the thymeleaf template processor
 */
@Configuration
public class ThymeleafConfiguration {

    @Value("${alma.register.datadir:#{systemProperties['user.home']}/.almaregister/}")
    private String localTemplateFolder;

    private final SpringTemplateEngine springTemplateEngine;

    /**
     * constructor based autowiring
     *
     * @param springTemplateEngine the template engine used by spring
     */
    ThymeleafConfiguration(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    @Bean
    @Description("Thymeleaf View Resolver")
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(springTemplateEngine);
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setOrder(1);
        return viewResolver;
    }

    /**
     * sets the template folder to the local template folder.
     */
    @PostConstruct
    public void extension() {
        FileTemplateResolver localResolver = new FileTemplateResolver();
        localResolver.setPrefix(localTemplateFolder);
        localResolver.setSuffix(".html");
        localResolver.setTemplateMode("HTML");
        localResolver.setOrder(1);
        localResolver.setCharacterEncoding("UTF-8");
        localResolver.setOrder(this.springTemplateEngine.getTemplateResolvers().size());
        localResolver.setCacheable(false);
        this.springTemplateEngine.addTemplateResolver(localResolver);
        this.springTemplateEngine.addDialect(new LayoutDialect());
    }

}
