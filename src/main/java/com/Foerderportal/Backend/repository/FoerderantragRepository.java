package com.Foerderportal.Backend.repository;

import com.Foerderportal.Backend.model.Foerderantrag;
import com.Foerderportal.Backend.model.AntragStatus;
import com.Foerderportal.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoerderantragRepository extends JpaRepository<Foerderantrag, Long> {
    List<Foerderantrag> findByAntragsteller(User antragsteller);
    List<Foerderantrag> findByStatus(AntragStatus status);
    List<Foerderantrag> findByAntragstellerAndStatus(User antragsteller, AntragStatus status);
}