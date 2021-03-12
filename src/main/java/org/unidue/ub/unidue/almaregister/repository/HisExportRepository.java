package org.unidue.ub.unidue.almaregister.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unidue.ub.unidue.almaregister.model.HisExport;

@Repository
public interface HisExportRepository extends CrudRepository<HisExport, String> {

    HisExport findByZimKennung(String zimKennung);

    HisExport findByMtknr(String matrikel);

    long count();
}
