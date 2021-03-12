package org.unidue.ub.unidue.almaregister.jobs.his;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;
import org.unidue.ub.unidue.almaregister.model.HisExport;
import org.unidue.ub.unidue.almaregister.repository.HisExportRepository;
import org.unidue.ub.unidue.almaregister.service.HisService;

import java.util.List;

@Service
public class HisExportWriter implements ItemWriter<HisExport> {

    private final HisService hisService;

    HisExportWriter(HisService hisService) {
        this.hisService = hisService;
    }

    @Override
    public void write(List<? extends HisExport> list) {
        for (HisExport hisExport: list) {
            this.hisService.save(hisExport);
        }
    }
}
