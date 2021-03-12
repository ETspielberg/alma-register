package org.unidue.ub.unidue.almaregister.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
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

/**
 * Main controller for scheduled jobs to be executed regularly
 */
@Controller
@EnableScheduling
public class JobLauncherController {

    private final JobLauncher jobLauncher;

    private final Job hisJob;

    @Value("${alma.register.datadir:#{systemProperties['user.home']}/.almaregister/}")
    private String dataDir;

    @Value("${his.data.url:localhost/files/his}")
    private String targetUrl;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private final HisExportRepository hisExportRepository;

    /**
     * constructor based autowiring
     * @param jobLauncher the launcher for the jobs to be run
     * @param hisJob the job collecting the students data from the web address
     * @param hisExportRepository the repository to store the collected student data
     */
    JobLauncherController(JobLauncher jobLauncher, Job hisJob, HisExportRepository hisExportRepository) {
        this.jobLauncher = jobLauncher;
        this.hisJob = hisJob;
        this.hisExportRepository = hisExportRepository;
    }

    /**
     * the job collecting the students from the web address and storing it into the database
     * @throws JobParametersInvalidException thrown if invalid paramters are provided
     * @throws JobExecutionAlreadyRunningException thrown if the job is already running
     * @throws JobRestartException thrown if the job restart interferes
     * @throws JobInstanceAlreadyCompleteException thrown if the job is already completed
     */
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

    /**
     * the job collecting the students data for a given date from the web address and storing it into the database
     * @param date the date to retrieve the data for
     * @throws JobParametersInvalidException thrown if invalid paramters are provided
     * @throws JobExecutionAlreadyRunningException thrown if the job is already running
     * @throws JobRestartException thrown if the job restart interferes
     * @throws JobInstanceAlreadyCompleteException thrown if the job is already completed
     */
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

    /**
     * retreive the students data for a given zim identifier
     * @param zimKennung the zim identifier
     * @return a list of students data
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("secure/test/{zimKennung}")
    public ResponseEntity<HisExport> getForZimKennung(@PathVariable String zimKennung) {
        return ResponseEntity.ok(this.hisExportRepository.getByZimKennung(zimKennung));
    }
}
