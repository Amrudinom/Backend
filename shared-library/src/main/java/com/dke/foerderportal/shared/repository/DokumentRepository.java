package com.dke.foerderportal.shared.repository;

import com.dke.foerderportal.shared.model.Dokument;
import com.dke.foerderportal.shared.model.Foerderantrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DokumentRepository extends JpaRepository<Dokument, Long> {
    List<Dokument> findByFoerderantrag(Foerderantrag foerderantrag);
}