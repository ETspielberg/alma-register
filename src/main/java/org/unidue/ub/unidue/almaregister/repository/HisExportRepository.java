package org.unidue.ub.unidue.almaregister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unidue.ub.unidue.almaregister.model.his.HisExport;


@Repository
public interface HisExportRepository extends JpaRepository<HisExport, String> {

    HisExport findByZimKennung(String zimKennung);

    HisExport findByMtknr(String matrikel);

    long count();
}
