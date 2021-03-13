package org.unidue.ub.unidue.almaregister.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
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
        LocalDate today = LocalDate.now();
        if (today.getDayOfWeek()== DayOfWeek.SUNDAY)
            today = today.minusDays(2);
        else if (today.getDayOfWeek()== DayOfWeek.SATURDAY)
            today = today.minusDays(1);
        String filename =  today.format(formatter) + "-1";
        jobParametersBuilder.addString("his.filename", filename).toJobParameters();
        jobParametersBuilder.addDate("date", new Date()).toJobParameters();
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();
        jobLauncher.run(hisJob, jobParameters);
    }
}
