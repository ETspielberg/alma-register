package org.unidue.ub.unidue.almaregister.controller;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.unidue.ub.unidue.almaregister.service.ScheduledService;

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
    @GetMapping("secure/importData")
    public void runHisImportForDate() throws Exception {
        this.scheduledService.runImportJob();
    }
}
