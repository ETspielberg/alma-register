package org.unidue.ub.unidue.almaregister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unidue.ub.unidue.almaregister.model.his.HisExport;

import java.util.List;

@Repository
public interface HisExportRepository extends JpaRepository<HisExport, String> {

    HisExport findByZimKennung(String zimKennung);

    List<HisExport> saveAll(List<HisExport> list);

    HisExport findByMtknr(String matrikel);

    long count();
}
