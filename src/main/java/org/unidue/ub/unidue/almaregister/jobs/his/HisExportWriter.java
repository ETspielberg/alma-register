package org.unidue.ub.unidue.almaregister.jobs.his;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;
import org.unidue.ub.unidue.almaregister.model.HisExport;
import org.unidue.ub.unidue.almaregister.repository.HisExportRepository;

import java.util.List;

@Service
public class HisExportWriter implements ItemWriter<HisExport> {

    private final HisExportRepository hisExportRepository;

    HisExportWriter(HisExportRepository hisExportRepository) {
        this.hisExportRepository = hisExportRepository;
    }

    @Override
    public void write(List<? extends HisExport> list) throws Exception {
        for (HisExport hisExport: list) {
            this.hisExportRepository.save(hisExport);
        }
    }
}
