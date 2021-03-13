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

    @Transactional
    public void clear() {
        this.hisExportRepository.deleteAll();
    }

    @Transactional
    public void save(HisExport hisExport) {
        this.hisExportRepository.save(hisExport);
    }

    public HisExport getByZimId(String zimId) {
        log.info("retreiving HIS data by ZIM-ID " + zimId);
        return this.hisExportRepository.findByZimKennung(zimId);
    }

    public HisExport getByMatrikel(String matrikel) {
        log.info("retreiving HIS data by matrikel " + matrikel);
        return this.hisExportRepository.findByMtknr(matrikel);
    }

    public long countAll() {
        return this.hisExportRepository.count();
    }

    @Transactional
    public void saveAll(List<HisExport> list) {
        this.hisExportRepository.saveAll(list);
    }
}
