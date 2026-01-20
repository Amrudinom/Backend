package com.dke.foerderportal.shared.service;

import com.dke.foerderportal.shared.dto.CreateAntragRequest;
import com.dke.foerderportal.shared.dto.FoerderantragDetailDto;
import com.dke.foerderportal.shared.dto.FoerderantragListDto;
import com.dke.foerderportal.shared.model.AntragStatus;
import com.dke.foerderportal.shared.model.Foerderantrag;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.repository.FoerderantragRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dke.foerderportal.shared.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FoerderantragService {

    private final FoerderantragRepository foerderantragRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public List<FoerderantragListDto> getMyAntraegeDtos(User user) {
        return foerderantragRepository.findListDtosByUserId(user.getId());
    }

    public FoerderantragDetailDto getAntragDetailDtoById(Long id) {
        FoerderantragDetailDto dto = foerderantragRepository.findDetailDtoById(id);
        if (dto == null) throw new RuntimeException("Foerderantrag not found: " + id);
        return dto;
    }

    public List<Foerderantrag> getAllAntraege() {
        return foerderantragRepository.findAll();
    }

    public List<Foerderantrag> getAntraegeByUser(User user) {
        return foerderantragRepository.findByAntragsteller(user);
    }

    public List<Foerderantrag> getAntraegeByStatus(AntragStatus status) {
        return foerderantragRepository.findByStatus(status);
    }

    public Foerderantrag getAntragById(Long id) {
        return foerderantragRepository.findById(id).orElseThrow(() -> new RuntimeException("Foerderantrag not found: " + id));
    }

    public Foerderantrag createAntrag(Foerderantrag antrag, Long antragstellerId) {
        User antragstellerRef = userRepository.getReferenceById(antragstellerId);
        antrag.setAntragsteller(antragstellerRef);
        antrag.setStatus(AntragStatus.EINGEREICHT);
        antrag.setEingereichtAm(LocalDateTime.now());


        Foerderantrag saved = foerderantragRepository.save(antrag);
        log.info("Foerderantrag created: {}", saved.getId());

        // Send email notification (don't fail if email fails)
        try {
            emailService.sendAntragEingereichtEmail(antrag.getAntragsteller().getEmail(), antrag.getTitel());
        } catch (Exception e) {
            log.error("Failed to send email notification", e);
        }

        return saved;
    }

    public Foerderantrag updateAntrag(Long id, Foerderantrag antragDetails) {
        Foerderantrag antrag = getAntragById(id);

        antrag.setTitel(antragDetails.getTitel());
        antrag.setBeschreibung(antragDetails.getBeschreibung());
        antrag.setBetrag(antragDetails.getBetrag());

        return foerderantragRepository.save(antrag);
    }

    public Foerderantrag genehmigenAntrag(Long id, User bearbeiter) {
        return updateStatus(id, AntragStatus.GENEHMIGT, bearbeiter, null);
    }

    public Foerderantrag ablehnenAntrag(Long id, User bearbeiter, String grund) {
        return updateStatus(id, AntragStatus.ABGELEHNT, bearbeiter, grund);
    }


    public void deleteAntrag(Long id) {
        foerderantragRepository.deleteById(id);
        log.info("Foerderantrag deleted: {}", id);
    }

    public List<CreateAntragRequest> filterAntraege(AntragStatus status, Long userId, LocalDate from, LocalDate to) {
        List<Foerderantrag> result;

        if (userId != null) {
            if (status != null) {
                result = foerderantragRepository.findByAntragstellerAndStatus(userRepository.getReferenceById(userId), status);
            } else {
                result = foerderantragRepository.findByAntragsteller_Id(userId);
            }
        } else {
            if (status != null) {
                result = foerderantragRepository.findByStatus(status);
            } else {
                result = foerderantragRepository.findAll();
            }
        }

        if (from != null && to != null) {
            LocalDateTime fromDateTime = from.atStartOfDay();
            LocalDateTime toDateTime = to.atTime(23, 59, 59);
            result = result.stream().filter(a -> a.getEingereichtAm() != null && !a.getEingereichtAm().isBefore(fromDateTime) && !a.getEingereichtAm().isAfter(toDateTime)).collect(Collectors.toList());
        }

        return result.stream().map(a -> new CreateAntragRequest(a.getId(), a.getTitel(), a.getBeschreibung(), a.getBetrag(), a.getStatus(), a.getEingereichtAm(), a.getAntragsteller() != null ? a.getAntragsteller().getName() : null)).collect(Collectors.toList());
    }

    public Foerderantrag updateStatus(Long id, AntragStatus newStatus, User bearbeiter, String grundOptional) {
        Foerderantrag antrag = getAntragById(id);

        antrag.setStatus(newStatus);
        antrag.setBearbeiter(bearbeiter);
        antrag.setBearbeitetAm(LocalDateTime.now());

        if (newStatus == AntragStatus.ABGELEHNT) {
            antrag.setAblehnungsgrund(grundOptional);
        } else {
            antrag.setAblehnungsgrund(null);
        }

        Foerderantrag saved = foerderantragRepository.save(antrag);

        try {
            String to = antrag.getAntragsteller().getEmail();
            String titel = antrag.getTitel();

            switch (newStatus) {
                case IN_BEARBEITUNG -> emailService.sendAntragInBearbeitungEmail(to, titel);
                case GENEHMIGT -> emailService.sendAntragGenehmigtEmail(to, titel);
                case ABGELEHNT ->
                        emailService.sendAntragAbgelehntEmail(to, titel, grundOptional != null ? grundOptional : "-");
                default -> { /* keine Mail */ }
            }
        } catch (Exception e) {
            log.error("Failed to send email notification", e);
        }

        return saved;
    }


}