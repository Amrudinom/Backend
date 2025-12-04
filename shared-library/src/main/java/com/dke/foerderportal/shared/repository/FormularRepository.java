package com.dke.foerderportal.shared.repository;

import com.dke.foerderportal.shared.model.Formular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FormularRepository extends JpaRepository<Formular, Long> {
    List<Formular> findByIstVeroeffentlichtTrue();
    List<Formular> findByKategorie(String kategorie);
    List<Formular> findByIstVeroeffentlichtTrueAndKategorie(String kategorie);
}