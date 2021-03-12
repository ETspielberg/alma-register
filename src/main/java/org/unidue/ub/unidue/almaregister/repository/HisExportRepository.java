package org.unidue.ub.unidue.almaregister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unidue.ub.unidue.almaregister.model.HisExport;

import java.util.List;

@Repository
public interface HisExportRepository extends JpaRepository<HisExport, String> {

    HisExport save(HisExport hisExport);

    HisExport getByZimKennung(String zimKennung);

    HisExport findByZimKennung(String zimKennung);
}
