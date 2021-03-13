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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.unidue.ub.unidue.almaregister.repository.HisExportRepository;
import org.unidue.ub.unidue.almaregister.service.ScheduledService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Main controller for scheduled jobs to be executed regularly
 */
@Controller
@EnableScheduling
public class JobLauncherController {

    private final ScheduledService scheduledService;

    /**
     * constructor based autowiring
     * @param scheduledService the service running the jobs on schedule
     */
    JobLauncherController(ScheduledService scheduledService) {
        this.scheduledService = scheduledService;
    }

    /**
     * the job collecting the students data for a given date from the web address and storing it into the database
     * @throws Exception thrown if the job fails
     */
    @PostMapping("secure/importData")
    public void runHisImportForDate() throws Exception {
        this.scheduledService.runImportJob();
    }
}
