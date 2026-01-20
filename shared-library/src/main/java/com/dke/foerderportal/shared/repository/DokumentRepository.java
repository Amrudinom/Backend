package com.dke.foerderportal.shared.repository;

import com.dke.foerderportal.shared.model.Dokument;
import com.dke.foerderportal.shared.model.Foerderantrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.dke.foerderportal.shared.dto.DokumentDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DokumentRepository extends JpaRepository<Dokument, Long> {

    @Query("""
        select new com.dke.foerderportal.shared.dto.DokumentDto(
            d.id,
            d.filename,
            d.contentType,
            d.fileSize,
            cast(d.uploadedAt as string),
            d.uploadedBy.id
        )
        from Dokument d
        where d.foerderantrag.id = :antragId
        order by d.uploadedAt desc
    """)
    List<DokumentDto> findDtosByAntragId(@Param("antragId") Long antragId);
    List<Dokument> findByFoerderantrag(Foerderantrag foerderantrag);
}

