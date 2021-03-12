package org.unidue.ub.unidue.almaregister.jobs.his;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@StepScope
public class FileReader implements ItemReader<String> {

    @Value("#{jobParameters['his.filename'] ?: ''}")
    private String filename;

    @Value("${libintel.data.dir}")
    public String dataDir;

    private Boolean collected = false;

    private List<String> lines;

    private final static Logger log = LoggerFactory.getLogger(FileReader.class);

    FileReader() {
    }

    @Override
    public String read() throws Exception{
        if (!collected)
            readLines();
        if (!lines.isEmpty())
            return lines.remove(0);
        return null;
    }

    private void readLines() throws IOException {
        lines = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(dataDir + this.filename));
        String readLine;
        while ((readLine = bufferedReader.readLine()) != null ) {
            if (!readLine.isEmpty()) {
                if (readLine.toLowerCase().startsWith("Mtknr#")) {
                    log.info("skipping line");
                    continue;
                }
                lines.add(readLine);
            }
        }
        bufferedReader.close();
        collected = true;
        log.info("read " + lines.size() + " lines of data");
    }
}
