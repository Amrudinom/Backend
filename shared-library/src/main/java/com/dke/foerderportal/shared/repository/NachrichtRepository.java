package com.dke.foerderportal.shared.repository;

import com.dke.foerderportal.shared.model.Nachricht;
import com.dke.foerderportal.shared.model.Foerderantrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NachrichtRepository extends JpaRepository<Nachricht, Long> {
    List<Nachricht> findByFoerderantragOrderByGesendetAmAsc(Foerderantrag foerderantrag);
    List<Nachricht> findByFoerderantragIdOrderByGesendetAmAsc(Long foerderantragId);
}