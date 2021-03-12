package org.unidue.ub.unidue.almaregister.jobs.his;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

@StepScope
@Component
public class FieldDeterminerTasklet implements Tasklet {

    private final Logger log = LoggerFactory.getLogger(FieldDeterminerTasklet.class);

    private Map<String, Integer> fieldMap;

    @Value("${libintel.data.dir}")
    public String dataDir;

    @Value("#{jobParameters['his.filename'] ?: ''}")
    private String filename;

    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) {
        log.info("preparing field indices for file " + filename);
        ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        this.fieldMap = new HashMap<>();
        Integer initialLines = 0;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dataDir + filename));
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                readLine = readLine.toLowerCase();
                initialLines++;
                if (readLine.contains("mtknr")) {
                    prepareMapsFromLine(readLine);
                    break;
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        jobExecutionContext.put("fieldMap", fieldMap);
        jobExecutionContext.put("inital.lines", initialLines);
        return RepeatStatus.FINISHED;
    }

    private void prepareMapsFromLine(String readLine) {
        String[] parts = readLine.split("#");
        for (int i = 0; i < parts.length; i++) {
            switch (parts[i]) {
                case "mtknr": {
                    fieldMap.put("mtknr", i);
                }
                case "zim-kennung": {
                    fieldMap.put("zimKennung", i);
                    break;
                }
                case "bibkz": {
                    fieldMap.put("bibkz", i);
                    break;
                }
                case "chip-folgenr": {
                    fieldMap.put("cardCurrens", i);
                    break;
                }
                case "geschl": {
                    fieldMap.put("geschl", i);
                    break;
                }
                case "nachname": {
                    fieldMap.put("nachname", i);
                    break;
                }
                case "vorname": {
                    fieldMap.put("vorname", i);
                    break;
                }
                case "gebdat": {
                    fieldMap.put("gebdat", i);
                    break;
                }
                case "straße": {
                    fieldMap.put("strasse", i);
                    break;
                }
                case "pozusatz": {
                        fieldMap.put("pozusatz", i);
                    break;
                }
                case "plz": {
                        fieldMap.put("plz", i);
                    break;
                }
                case "ort": {
                    fieldMap.put("ort", i);
                    break;
                }
                case "land": {
                    fieldMap.put("land", i);
                    break;
                }
                case "festnetz": {
                    fieldMap.put("festnetz", i);
                    break;
                }
                case "mobil": {
                    fieldMap.put("mobil", i);
                    break;
                }
                case "email": {
                    fieldMap.put("email", i);
                    break;
                }
                case "immadatum": {
                    fieldMap.put("immadatum", i);
                    break;
                }
                case "status": {
                    fieldMap.put("status", i);
                    break;
                }
                case "semester": {
                    fieldMap.put("semester", i);
                    break;
                }
                case "campus": {
                    fieldMap.put("campus", i);
                    break;
                }
                case "exgr": {
                    fieldMap.put("exgr", i);
                    break;
                }
                case "exmagrund": {
                    fieldMap.put("exmagrund", i);
                    break;
                }
                case "exmadatum": {
                    fieldMap.put("exmadatum", i);
                    break;
                }
                case "abschluss1": {
                    fieldMap.put("abschluss", i);
                    break;
                }
                case "fach1": {
                    fieldMap.put("fach1", i);
                    break;
                }
                case "fach2": {
                    fieldMap.put("fach2", i);
                    break;
                }
                case "fach3": {
                    fieldMap.put("fach3", i);
                    break;
                }
                case "exmaantrag": {
                    fieldMap.put("exmaantrag", i);
                    break;
                }
                case "hörerstatus": {
                    fieldMap.put("hoererstatus", i);
                    break;
                }
            }
        }
    }
}
