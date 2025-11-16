package com.Foerderportal.Backend.repository;

import com.Foerderportal.Backend.model.Formular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FormularRepository extends JpaRepository<Formular, Long> {
    List<Formular> findByIstVeroeffentlichtTrue();
    List<Formular> findByKategorie(String kategorie);
    List<Formular> findByIstVeroeffentlichtTrueAndKategorie(String kategorie);
}