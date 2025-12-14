package com.dke.foerderportal.shared.repository;

import com.dke.foerderportal.shared.model.Foerderantrag;
import com.dke.foerderportal.shared.model.AntragStatus;
import com.dke.foerderportal.shared.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FoerderantragRepository extends JpaRepository<Foerderantrag, Long> {
    List<Foerderantrag> findByAntragsteller(User antragsteller);
    List<Foerderantrag> findByStatus(AntragStatus status);
    List<Foerderantrag> findByAntragstellerAndStatus(User antragsteller, AntragStatus status);
    List<Foerderantrag> findByEingereichtAmBetween(LocalDateTime from, LocalDateTime to);
    List<Foerderantrag> findByAntragsteller_Id(Long antragstellerId);
    List<Foerderantrag> findByAntragsteller_NameContainingIgnoreCase(String name);

}