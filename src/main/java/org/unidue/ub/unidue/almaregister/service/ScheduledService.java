package org.unidue.ub.unidue.almaregister.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class ScheduledService {

    private final JobLauncher jobLauncher;

    private final Job hisJob;

    ScheduledService(JobLauncher jobLauncher,
                     Job hisJob) {
        this.jobLauncher = jobLauncher;
        this.hisJob = hisJob;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void runImportJob() throws Exception {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String filename =  LocalDate.now().format(formatter) + "-1";
        jobParametersBuilder.addString("his.filename", filename).toJobParameters();
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();
        jobLauncher.run(hisJob, jobParameters);
    }
}
