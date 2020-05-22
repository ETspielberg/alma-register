package org.unidue.ub.unidue.almaregister.jobs.his;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unidue.ub.unidue.almaregister.model.HisExport;
import org.unidue.ub.unidue.almaregister.repository.HisExportRepository;

@Configuration
@EnableBatchProcessing
public class HisConfiguration {

    @Value("${his.data.username}")
    private String username;

    @Value("${his.data.password}")
    private String password;

    public final JobBuilderFactory jobBuilderFactory;

    public final StepBuilderFactory stepBuilderFactory;

    private final HisExportRepository hisExportRepository;

    HisConfiguration(HisExportRepository hisExportRepository,
                     StepBuilderFactory stepBuilderFactory,
                     JobBuilderFactory jobBuilderFactory) {
        this.hisExportRepository = hisExportRepository;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    CollectFileTasklet collectFileTasklet() {
        return new CollectFileTasklet()
                .withPasssword(this.password)
                .withUsername(this.username);
    }

    @Bean
    FieldDeterminerTasklet fieldDeterminerTasklet() {
        return new FieldDeterminerTasklet();
    }

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
        return new HisExportWriter(this.hisExportRepository);
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
                .next(hisConvertStep())
                .build();
    }

}
