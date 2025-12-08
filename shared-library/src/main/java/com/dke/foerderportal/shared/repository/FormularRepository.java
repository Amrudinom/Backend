package com.dke.foerderportal.shared.repository;

import com.dke.foerderportal.shared.model.Formular;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormularRepository extends JpaRepository<Formular, Long> {

    @EntityGraph(attributePaths = {"felder"})
    Optional<Formular> findById(Long id);

    List<Formular> findByIstVeroeffentlichtTrue();
    List<Formular> findByKategorie(String kategorie);
    List<Formular> findByIstVeroeffentlichtTrueAndKategorie(String kategorie);
}