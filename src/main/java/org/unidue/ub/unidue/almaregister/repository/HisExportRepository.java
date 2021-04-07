package org.unidue.ub.unidue.almaregister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unidue.ub.unidue.almaregister.model.his.HisExport;


@Repository
public interface HisExportRepository extends JpaRepository<HisExport, String> {

    HisExport findByZimKennung(String zimKennung);

    HisExport findByMtknr(String matrikel);

    long count();

    @Modifying
    @Query("UPDATE HisExport h SET h.contained = :contained, h.changed = :changed, h.exmatriculated = :exmatriculated")
    void updateChangedAndContainedAndExmatriculated(@Param("contained") boolean contained, @Param("changed") boolean changed, @Param("exmatriculated") boolean exmatriculated);
}
