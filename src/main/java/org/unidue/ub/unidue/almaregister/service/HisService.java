package org.unidue.ub.unidue.almaregister.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.unidue.ub.unidue.almaregister.model.his.HisExport;
import org.unidue.ub.unidue.almaregister.repository.HisExportRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class HisService {

    private final HisExportRepository hisExportRepository;

    private final Logger log = LoggerFactory.getLogger(HisService.class);

    HisService(HisExportRepository hisExportRepository) {
        this.hisExportRepository = hisExportRepository;
    }

    public HisExport getByZimId(String zimId) {
        log.info("retreiving HIS data by ZIM-ID " + zimId);
        return this.hisExportRepository.findByZimKennung(zimId);
    }

    public long countAll() {
        return this.hisExportRepository.count();
    }

    @Transactional
    public void saveAll(List<HisExport> list) {
        for (HisExport hisExport: list) {
            HisExport oldEntry = this.hisExportRepository.findByZimKennung(hisExport.getZimKennung());
            if (oldEntry == null)
                this.hisExportRepository.save(hisExport);
            else {
                if (oldEntry.updateEntry(hisExport))
                    this.hisExportRepository.save(oldEntry);
            }
        }
    }

    @Transactional
    public void setFieldsForNewRun() {
        this.hisExportRepository.updateChangedAndContainedAndExmatriculated(false, false, true);
    }
}
