package org.unidue.ub.unidue.almaregister.service;

import org.springframework.stereotype.Service;
import org.unidue.ub.unidue.almaregister.model.HisExport;
import org.unidue.ub.unidue.almaregister.repository.HisExportRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class HisService {

    private final HisExportRepository hisExportRepository;

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
        return this.hisExportRepository.getByZimKennung(zimId);
    }
}
