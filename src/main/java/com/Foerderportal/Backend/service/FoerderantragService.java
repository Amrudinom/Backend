package com.Foerderportal.Backend.service;

import com.Foerderportal.Backend.model.AntragStatus;
import com.Foerderportal.Backend.model.Foerderantrag;
import com.Foerderportal.Backend.model.User;
import com.Foerderportal.Backend.repository.FoerderantragRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.Foerderportal.Backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FoerderantragService {

    private final FoerderantragRepository foerderantragRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

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
        return foerderantragRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foerderantrag not found: " + id));
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
            emailService.sendAntragEingereichtEmail(
                    antrag.getAntragsteller().getEmail(),
                    antrag.getTitel()
            );
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
        Foerderantrag antrag = getAntragById(id);

        antrag.setStatus(AntragStatus.GENEHMIGT);
        antrag.setBearbeiter(bearbeiter);
        antrag.setBearbeitetAm(LocalDateTime.now());

        Foerderantrag saved = foerderantragRepository.save(antrag);
        log.info("Foerderantrag approved: {}", id);

        // Send email notification
        try {
            emailService.sendAntragGenehmigtEmail(
                    antrag.getAntragsteller().getEmail(),
                    antrag.getTitel()
            );
        } catch (Exception e) {
            log.error("Failed to send email notification", e);
        }

        return saved;
    }

    public Foerderantrag ablehnenAntrag(Long id, User bearbeiter, String grund) {
        Foerderantrag antrag = getAntragById(id);

        antrag.setStatus(AntragStatus.ABGELEHNT);
        antrag.setBearbeiter(bearbeiter);
        antrag.setBearbeitetAm(LocalDateTime.now());
        antrag.setAblehnungsgrund(grund);

        Foerderantrag saved = foerderantragRepository.save(antrag);
        log.info("Foerderantrag rejected: {}", id);

        // Send email notification
        try {
            emailService.sendAntragAbgelehntEmail(
                    antrag.getAntragsteller().getEmail(),
                    antrag.getTitel(),
                    grund
            );
        } catch (Exception e) {
            log.error("Failed to send email notification", e);
        }

        return saved;
    }

    public void deleteAntrag(Long id) {
        foerderantragRepository.deleteById(id);
        log.info("Foerderantrag deleted: {}", id);
    }
}