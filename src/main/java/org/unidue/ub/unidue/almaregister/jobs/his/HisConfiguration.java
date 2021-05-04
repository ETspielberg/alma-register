package org.unidue.ub.unidue.almaregister.jobs.his;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unidue.ub.unidue.almaregister.model.his.HisExport;
import org.unidue.ub.unidue.almaregister.service.HisService;

@Configuration
@EnableBatchProcessing
public class HisConfiguration {

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    private final HisService hisService;

    HisConfiguration(StepBuilderFactory stepBuilderFactory,
                     JobBuilderFactory jobBuilderFactory,
                     HisService hisService) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobBuilderFactory = jobBuilderFactory;
        this.hisService = hisService;
    }

    @Bean
    CollectFileTasklet collectFileTasklet() {
        return new CollectFileTasklet();
    }

    @Bean
    FieldDeterminerTasklet fieldDeterminerTasklet() {
        return new FieldDeterminerTasklet();
    }

    @Bean
    ClearTableTasklet clearTableTasklet() {return new ClearTableTasklet(hisService);}

    @Bean
    FileReader fileReader() {
        return new FileReader();
    }

    @Bean
    HisLineProcessor hisLineProcessor() {
        return new HisLineProcessor();
    }

    @Bean
    HisExportWriter hisExportWriter() {
        return new HisExportWriter(this.hisService);
    }

    @Bean
    public Step downloadFile() {
        return stepBuilderFactory.get("downloadFile")
                .tasklet(collectFileTasklet())
                .build();
    }

    @Bean
    public Step prepareMap() {
        return stepBuilderFactory.get("prepareMap")
                .tasklet(fieldDeterminerTasklet())
                .build();
    }

    @Bean
    public Step clearTable() {
        return stepBuilderFactory.get("clearTable")
                .tasklet(clearTableTasklet())
                .build();
    }

    @Bean
    public Step hisConvertStep() {
        return stepBuilderFactory.get("hisConvertStep")
                .<String, HisExport>chunk(10000)
                .reader(fileReader())
                .processor(hisLineProcessor())
                .writer(hisExportWriter())
                .build();
    }

    @Bean
    public Job hisJob () {
        return jobBuilderFactory.get("hisJob")
                .start(downloadFile())
                .next(prepareMap())
                .next(clearTable())
                .next(hisConvertStep())
                .build();
    }

}
