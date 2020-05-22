package org.unidue.ub.unidue.almaregister.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.unidue.ub.unidue.almaregister.model.HisExport;
import org.unidue.ub.unidue.almaregister.repository.HisExportRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@EnableScheduling
public class JobLauncherController {

    private final JobLauncher jobLauncher;

    private final Job hisJob;

    @Value("${libintel.data.dir}")
    private String dataDir;

    @Value("${his.data.url}")
    private String targetUrl;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private final HisExportRepository hisExportRepository;

    JobLauncherController(JobLauncher jobLauncher, Job hisJob, HisExportRepository hisExportRepository) {
        this.jobLauncher = jobLauncher;
        this.hisJob = hisJob;
        this.hisExportRepository = hisExportRepository;
    }

    @Scheduled(cron = "0 55 23 * * ?")
    public void runHisImport() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        Date now = new Date();
        jobParametersBuilder
                .addLong("time", System.currentTimeMillis())
                .addString("his.date", dateFormat.format(now))
                .addString("his.filename", this.dataDir + "/" + dateFormat.format(now) + "_download.txt")
                .addString("his.url", this.targetUrl + dateFormat.format(now) + "-1")
                .toJobParameters();
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();
        jobLauncher.run(hisJob, jobParameters);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("secure/importData")
    public void runHisImportForDate(@RequestParam String date) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder
                .addLong("time", System.currentTimeMillis())
                .addString("his.date", date)
                .addString("his.filename", this.dataDir + "/" + date + "_download.txt")
                .addString("his.url", this.targetUrl + date + "-1")
                .toJobParameters();
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();
        jobLauncher.run(hisJob, jobParameters);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("secure/test/{zimKennung}")
    public ResponseEntity<List<HisExport>> getForZimKennung(@PathVariable String zimKennung) {
        return ResponseEntity.ok(this.hisExportRepository.findAllByZimKennung(zimKennung));
    }
}