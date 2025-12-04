package com.dke.foerderportal.shared.repository;

import com.dke.foerderportal.shared.model.Formularvorlage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FormularvorlageRepository extends JpaRepository<Formularvorlage, Long> {
    List<Formularvorlage> findByIstVeroeffentlichtTrue();
    List<Formularvorlage> findByKategorie(String kategorie);
}