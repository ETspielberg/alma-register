package org.unidue.ub.unidue.almaregister.jobs.his;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import org.unidue.ub.unidue.almaregister.model.his.HisExport;

import java.util.Map;

@Service
public class HisLineProcessor implements ItemProcessor<String, HisExport> {

    private Map<String, Integer> fieldMap;

    @Override
    public HisExport process(String line) {
        String[] parts = line.split("#");
        HisExport hisExport = new HisExport();
        hisExport.setBibkz(getValue(parts, "bibkz"));
        hisExport.setCampus(getValue(parts,"campus"));
        hisExport.setEmail(getValue(parts,"email"));
        hisExport.setExgr(getValue(parts,"exgr"));
        hisExport.setExmaantrag(getValue(parts,"exmaantrag"));
        hisExport.setExmadatum(getValue(parts,"exmadatum"));
        hisExport.setExmagrund(getValue(parts,"exmagrund"));
        hisExport.setFestnetz(getValue(parts,"festnetz"));
        hisExport.setGebdat(getValue(parts,"gebdat"));
        hisExport.setGeschl(getValue(parts,"geschl"));
        hisExport.setImmadatum(getValue(parts,"immadatum"));
        hisExport.setLand(getValue(parts,"land"));
        hisExport.setMobil(getValue(parts,"mobil"));
        hisExport.setMtknr(getValue(parts,"mtknr"));
        hisExport.setNachname(getValue(parts,"nachname"));
        hisExport.setOrt(getValue(parts,"ort"));
        hisExport.setPlz(getValue(parts,"plz"));
        hisExport.setPozusatz(getValue(parts,"pozusatz"));
        hisExport.setSemester(getValue(parts,"semester"));
        hisExport.setStatus(getValue(parts,"status"));
        hisExport.setStrasse(getValue(parts,"strasse"));
        hisExport.setVorname(getValue(parts,"vorname"));
        hisExport.setZimKennung(getValue(parts,"zimKennung"));
        hisExport.setCardCurrens(getValue(parts, "cardCurrens"));
        hisExport.setAbschluss1(getValue(parts, "abschluss"));
        return hisExport;
    }

    private String getValue(String[] parts, String field) {
        try {
            return parts[fieldMap.get(field)];
        } catch (Exception e) {
            return "";
        }
    }

    @BeforeStep
    public void retrieveFieldmap(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        this.fieldMap = (Map<String, Integer>) jobContext.get("fieldMap");
    }
}
