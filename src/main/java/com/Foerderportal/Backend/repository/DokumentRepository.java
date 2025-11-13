package com.Foerderportal.Backend.repository;

import com.Foerderportal.Backend.model.Dokument;
import com.Foerderportal.Backend.model.Foerderantrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DokumentRepository extends JpaRepository<Dokument, Long> {
    List<Dokument> findByFoerderantrag(Foerderantrag foerderantrag);
}