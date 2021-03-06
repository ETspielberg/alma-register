package org.unidue.ub.unidue.almaregister.jobs.his;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.*;

@StepScope
@Component
public class CollectFileTasklet implements Tasklet {

    Logger log = LoggerFactory.getLogger(CollectFileTasklet.class);

    @Value("#{jobParameters['his.url'] ?: ''}")
    private String targetUrl;

    String username;

    String password;

    @Value("#{jobParameters['his.filename'] ?: ''}")
    private String filename;

    CollectFileTasklet() {
    }

    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) throws IOException {
        log.info("loading file from " + targetUrl + " and saving it to " + filename);
        Path filePath = Paths.get(filename);
        AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(
                filePath, READ, WRITE, CREATE);
        WebClient webClient = WebClient.builder()
                .baseUrl(targetUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
                .defaultHeader(HttpHeaders.USER_AGENT, "Spring 5 WebClient")
                .build();
        Flux<DataBuffer> incoming = webClient.get()
                //.header("Authorization", "Basic " + Base64Utils.encodeToString((this.username + ":" + this.password).getBytes(UTF_8)))
                .retrieve().bodyToFlux(DataBuffer.class);
        File writeOperation = DataBufferUtils.write(incoming, fileChannel)
                .map(DataBufferUtils::release)
                .then(Mono.just(filePath.toFile())).block();
        return RepeatStatus.FINISHED;
    }

    public CollectFileTasklet withUsername(String username) {
        this.username = username;
        return this;
    }

    public CollectFileTasklet withPasssword(String passsword) {
        this.password = passsword;
        return this;
    }
}
