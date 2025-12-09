package com.dke.foerderportal.shared.service;

import com.dke.foerderportal.shared.model.Nachricht;
import com.dke.foerderportal.shared.model.Foerderantrag;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.repository.NachrichtRepository;
import com.dke.foerderportal.shared.repository.FoerderantragRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NachrichtService {

    private final NachrichtRepository nachrichtRepository;
    private final FoerderantragRepository foerderantragRepository;

    /**
     * Alle Nachrichten zu einem Antrag laden
     */
    public List<Nachricht> getNachrichtenByAntragId(Long antragId) {
        return nachrichtRepository.findByFoerderantragIdOrderByGesendetAmAsc(antragId);
    }

    /**
     * Neue Nachricht erstellen
     */
    public Nachricht createNachricht(Long antragId, String inhalt, User sender) {
        Foerderantrag antrag = foerderantragRepository.findById(antragId)
                .orElseThrow(() -> new RuntimeException("Antrag nicht gefunden: " + antragId));

        Nachricht nachricht = new Nachricht();
        nachricht.setInhalt(inhalt);
        nachricht.setFoerderantrag(antrag);
        nachricht.setGesendetVon(sender);

        Nachricht saved = nachrichtRepository.save(nachricht);
        log.info("Nachricht erstellt für Antrag {}: {}", antragId, saved.getId());

        return saved;
    }

    /**
     * Nachricht löschen
     */
    public void deleteNachricht(Long id) {
        nachrichtRepository.deleteById(id);
        log.info("Nachricht gelöscht: {}", id);
    }
}