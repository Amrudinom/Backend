package com.Foerderportal.Backend.service;

import com.Foerderportal.Backend.model.Formular;
import com.Foerderportal.Backend.model.Formularfeld;
import com.Foerderportal.Backend.model.User;
import com.Foerderportal.Backend.repository.FormularRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FormularService {

    private final FormularRepository formularRepository;

    public List<Formular> getAllFormulare() {
        return formularRepository.findAll();
    }

    public List<Formular> getVeroeffentlichteFormulare() {
        return formularRepository.findByIstVeroeffentlichtTrue();
    }

    public List<Formular> getFormulareByKategorie(String kategorie) {
        return formularRepository.findByIstVeroeffentlichtTrueAndKategorie(kategorie);
    }

    public Formular getFormularById(Long id) {
        return formularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formular nicht gefunden: " + id));
    }

    public Formular createFormular(Formular formular, User ersteller) {
        formular.setErstellerVon(ersteller);
        formular.setIstVeroeffentlicht(false);

        if (formular.getFelder() != null) {
            for (int i = 0; i < formular.getFelder().size(); i++) {
                Formularfeld feld = formular.getFelder().get(i);
                feld.setAnzeigeReihenfolge(i);
                feld.setFormular(formular);
            }
        }

        Formular saved = formularRepository.save(formular);
        log.info("Formular erstellt: {}", saved.getId());
        return saved;
    }

    public Formular updateFormular(Long id, Formular formularDetails) {
        Formular formular = getFormularById(id);

        formular.setTitel(formularDetails.getTitel());
        formular.setBeschreibung(formularDetails.getBeschreibung());
        formular.setKategorie(formularDetails.getKategorie());
        formular.setSchema(formularDetails.getSchema());

        formular.getFelder().clear();
        if (formularDetails.getFelder() != null) {
            for (int i = 0; i < formularDetails.getFelder().size(); i++) {
                Formularfeld feld = formularDetails.getFelder().get(i);
                feld.setAnzeigeReihenfolge(i);
                formular.addFeld(feld);
            }
        }

        return formularRepository.save(formular);
    }

    public Formular veroeffentlichenFormular(Long id) {
        Formular formular = getFormularById(id);
        formular.setIstVeroeffentlicht(true);
        log.info("Formular veröffentlicht: {}", id);
        return formularRepository.save(formular);
    }

    public Formular zurueckziehenFormular(Long id) {
        Formular formular = getFormularById(id);
        formular.setIstVeroeffentlicht(false);
        log.info("Formular zurückgezogen: {}", id);
        return formularRepository.save(formular);
    }

    public void deleteFormular(Long id) {
        formularRepository.deleteById(id);
        log.info("Formular gelöscht: {}", id);
    }
}