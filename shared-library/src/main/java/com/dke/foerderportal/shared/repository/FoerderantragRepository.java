package com.dke.foerderportal.shared.repository;

import com.dke.foerderportal.shared.dto.FoerderantragDetailDto;
import com.dke.foerderportal.shared.dto.FoerderantragListDto;
import com.dke.foerderportal.shared.model.Foerderantrag;
import com.dke.foerderportal.shared.model.AntragStatus;
import com.dke.foerderportal.shared.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FoerderantragRepository extends JpaRepository<Foerderantrag, Long> {
    @Query("""
              select new com.dke.foerderportal.shared.dto.FoerderantragDetailDto(
                a.id,
                a.titel,
                a.beschreibung,
                a.betrag,
                a.status,
                a.eingereichtAm,
                a.bearbeitetAm,
                a.ablehnungsgrund,
                a.formularId,
                a.formularVersion,
                a.formularSnapshot,
                a.formularAntworten,
                s.id,
                s.name,
                b.id,
                b.name
              )
              from Foerderantrag a
              join a.antragsteller s
              left join a.bearbeiter b
              where a.id = :id
            """)
    FoerderantragDetailDto findDetailDtoById(@Param("id") Long id);

    @Query("""
              select new com.dke.foerderportal.shared.dto.FoerderantragListDto(
                a.id,
                a.titel,
                a.beschreibung,
                a.betrag,
                a.status,
                a.eingereichtAm
              )
              from Foerderantrag a
              where a.antragsteller.id = :userId
              order by a.eingereichtAm desc
            """)
    List<FoerderantragListDto> findListDtosByUserId(@Param("userId") Long userId);


    List<Foerderantrag> findByAntragsteller(User antragsteller);

    List<Foerderantrag> findByStatus(AntragStatus status);

    List<Foerderantrag> findByAntragstellerAndStatus(User antragsteller, AntragStatus status);

    List<Foerderantrag> findByEingereichtAmBetween(LocalDateTime from, LocalDateTime to);

    List<Foerderantrag> findByAntragsteller_Id(Long antragstellerId);

    List<Foerderantrag> findByAntragsteller_NameContainingIgnoreCase(String name);

}