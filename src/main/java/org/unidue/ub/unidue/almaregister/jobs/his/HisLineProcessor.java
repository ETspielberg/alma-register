package org.unidue.ub.unidue.almaregister.jobs.his;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import org.unidue.ub.unidue.almaregister.model.HisExport;

import java.util.Map;

@Service
public class HisLineProcessor implements ItemProcessor<String, HisExport> {

    private Map<String, Integer> fieldMap;

    @Override
    public HisExport process(String line) {
        String[] parts = line.split("#");
        if (parts.length < 22)
            return null;
        HisExport hisExport = new HisExport();
        hisExport.setBibkz(parts[fieldMap.get("bibkz")]);
        hisExport.setCampus(parts[fieldMap.get("campus")]);
        hisExport.setEmail(parts[fieldMap.get("email")]);
        hisExport.setExgr(parts[fieldMap.get("exgr")]);
        hisExport.setExmaantrag(parts[fieldMap.get("exmaantrag")]);
        hisExport.setExmadatum(parts[fieldMap.get("exmadatum")]);
        hisExport.setExmagrund(parts[fieldMap.get("exmagrund")]);
        hisExport.setFestnetz(parts[fieldMap.get("festnetz")]);
        hisExport.setGebdat(parts[fieldMap.get("gebdat")]);
        hisExport.setGeschl(parts[fieldMap.get("geschl")]);
        hisExport.setImmadatum(parts[fieldMap.get("immadatum")]);
        hisExport.setLand(parts[fieldMap.get("land")]);
        hisExport.setMobil(parts[fieldMap.get("mobil")]);
        hisExport.setMtknr(parts[fieldMap.get("mtknr")]);
        hisExport.setNachname(parts[fieldMap.get("nachname")]);
        hisExport.setOrt(parts[fieldMap.get("ort")]);
        hisExport.setPlz(parts[fieldMap.get("plz")]);
        hisExport.setPozusatz(parts[fieldMap.get("pozusatz")]);
        hisExport.setSemester(parts[fieldMap.get("semester")]);
        hisExport.setStatus(parts[fieldMap.get("status")]);
        hisExport.setStrasse(parts[fieldMap.get("strasse")]);
        hisExport.setVorname(parts[fieldMap.get("vorname")]);
        hisExport.setZimKennung(parts[fieldMap.get("zimKennung")]);
        return hisExport;
    }

    @BeforeStep
    public void retrieveFilename(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        this.fieldMap = (Map<String, Integer>) jobContext.get("fieldMap");
    }
}
