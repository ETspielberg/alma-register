package org.unidue.ub.unidue.almaregister.jobs.his;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.unidue.ub.unidue.almaregister.service.HisService;

@StepScope
@Component
public class ClearTableTasklet implements Tasklet {

    Logger log = LoggerFactory.getLogger(ClearTableTasklet.class);

    private final HisService hisService;

    ClearTableTasklet(HisService hisService) {
        this.hisService = hisService;
    }

    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) {
        this.hisService.clear();
        log.debug("cleared his export table");
        return RepeatStatus.FINISHED;
    }
}
