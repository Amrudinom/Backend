package com.dke.foerderportal.shared.service;

import com.dke.foerderportal.shared.model.Formular;
import com.dke.foerderportal.shared.model.FormularStatus;
import com.dke.foerderportal.shared.model.Formularfeld;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.repository.FormularRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FormularService {

    private final FormularRepository formularRepository;

    public List<Formular> getAllFormulare() {
        return formularRepository.findAllWithFelder();
    }

    public List<Formular> getVeroeffentlichteFormulare() {
        return formularRepository.findByIstVeroeffentlichtTrue();
    }

    public List<Formular> getFormulareByKategorie(String kategorie) {
        return formularRepository.findByIstVeroeffentlichtTrueAndKategorie(kategorie);
    }

    public Formular getFormularById(Long id) {
        return formularRepository.findByIdWithFelder(id)
                .orElseThrow(() -> new RuntimeException("Formular nicht gefunden: " + id));
    }

    public Formular createFormular(Formular formular, User ersteller) {
        formular.setErstellerVon(ersteller);

        if (formular.getStatus() == null) {
            formular.setStatus(FormularStatus.DRAFT);
        }

        if (formular.getIstVeroeffentlicht() == null) {
            formular.setIstVeroeffentlicht(formular.getStatus() == FormularStatus.PUBLISHED);
        }

        if (formular.getFelder() != null) {
            for (int i = 0; i < formular.getFelder().size(); i++) {
                Formularfeld feld = formular.getFelder().get(i);
                feld.setAnzeigeReihenfolge(i);
                feld.setFormular(formular);

                // WICHTIG: Setze alle Boolean Defaults
                if (feld.getOauthZuordbar() == null) {
                    feld.setOauthZuordbar(false);
                }
                if (feld.getOauthVorfeld() == null) {
                    feld.setOauthVorfeld(false);
                }
                if (feld.getOauthAutoFill() == null) {
                    feld.setOauthAutoFill(false);
                }
            }
        }

        Formular saved = formularRepository.save(formular);
        log.info("Formular erstellt mit Status: {}", saved.getStatus());
        return saved;
    }

    public Formular updateFormular(Long id, Formular formularDetails) {
        Formular formular = getFormularById(id);

        log.info("Aktualisiere Formular {} mit {} Feldern", id,
                formularDetails.getFelder() != null ? formularDetails.getFelder().size() : 0);

        // Basisdaten aktualisieren
        formular.setTitel(formularDetails.getTitel());
        formular.setBeschreibung(formularDetails.getBeschreibung());
        formular.setKategorie(formularDetails.getKategorie());
        formular.setSchema(formularDetails.getSchema());

        // Status aktualisieren
        if (formularDetails.getStatus() != null) {
            formular.setStatus(formularDetails.getStatus());
            formular.setIstVeroeffentlicht(formularDetails.getStatus() == FormularStatus.PUBLISHED);
        }

        // 1. Existierende Felder löschen, die nicht mehr in formularDetails sind
        List<Formularfeld> felderToRemove = new ArrayList<>();
        for (Formularfeld existingFeld : formular.getFelder()) {
            boolean stillExists = false;
            if (formularDetails.getFelder() != null) {
                for (Formularfeld newFeld : formularDetails.getFelder()) {
                    // Prüfe anhand der ID oder des feldName
                    if ((newFeld.getId() != null && newFeld.getId().equals(existingFeld.getId())) ||
                            (newFeld.getFeldName() != null && newFeld.getFeldName().equals(existingFeld.getFeldName()))) {
                        stillExists = true;
                        break;
                    }
                }
            }
            if (!stillExists) {
                felderToRemove.add(existingFeld);
            }
        }
        felderToRemove.forEach(formular::removeFeld);

        // 2. Neue Felder hinzufügen oder existierende aktualisieren
        if (formularDetails.getFelder() != null) {
            for (int i = 0; i < formularDetails.getFelder().size(); i++) {
                Formularfeld feldDetails = formularDetails.getFelder().get(i);

                // Suche nach existierendem Feld (erst nach ID, dann nach Name)
                Formularfeld existingFeld = null;
                if (feldDetails.getId() != null) {
                    existingFeld = formular.getFelder().stream()
                            .filter(f -> f.getId() != null && f.getId().equals(feldDetails.getId()))
                            .findFirst()
                            .orElse(null);
                }

                if (existingFeld == null && feldDetails.getFeldName() != null) {
                    existingFeld = formular.getFelder().stream()
                            .filter(f -> f.getFeldName() != null && f.getFeldName().equals(feldDetails.getFeldName()))
                            .findFirst()
                            .orElse(null);
                }

                if (existingFeld != null) {
                    // Aktualisiere existierendes Feld
                    existingFeld.setFeldName(feldDetails.getFeldName());
                    existingFeld.setFeldTyp(feldDetails.getFeldTyp());
                    existingFeld.setLabel(feldDetails.getLabel());
                    existingFeld.setPflichtfeld(feldDetails.isPflichtfeld());
                    existingFeld.setOauthAutoFill(feldDetails.getOauthAutoFill());
                    existingFeld.setOauthFieldMapping(feldDetails.getOauthFieldMapping());
                    existingFeld.setAnzeigeReihenfolge(i);
                    existingFeld.setPlaceholder(feldDetails.getPlaceholder());
                    existingFeld.setDefaultValue(feldDetails.getDefaultValue());
                    existingFeld.setMinLength(feldDetails.getMinLength());
                    existingFeld.setMaxLength(feldDetails.getMaxLength());
                    existingFeld.setRegexPattern(feldDetails.getRegexPattern());
                    existingFeld.setOptionen(feldDetails.getOptionen());
                    existingFeld.setCheckboxLabelTrue(feldDetails.getCheckboxLabelTrue());
                    existingFeld.setCheckboxLabelFalse(feldDetails.getCheckboxLabelFalse());
                    existingFeld.setFileTypes(feldDetails.getFileTypes());
                    existingFeld.setMaxFileSize(feldDetails.getMaxFileSize());
                    existingFeld.setMinValue(feldDetails.getMinValue());
                    existingFeld.setMaxValue(feldDetails.getMaxValue());
                } else {
                    // Füge neues Feld hinzu
                    Formularfeld newFeld = new Formularfeld();
                    newFeld.setFeldName(feldDetails.getFeldName());
                    newFeld.setFeldTyp(feldDetails.getFeldTyp());
                    newFeld.setLabel(feldDetails.getLabel());
                    newFeld.setPflichtfeld(feldDetails.isPflichtfeld());
                    newFeld.setOauthAutoFill(feldDetails.getOauthAutoFill() != null ? feldDetails.getOauthAutoFill() : false);
                    newFeld.setOauthFieldMapping(feldDetails.getOauthFieldMapping() != null ? feldDetails.getOauthFieldMapping() : "");
                    newFeld.setAnzeigeReihenfolge(i);
                    newFeld.setPlaceholder(feldDetails.getPlaceholder());
                    newFeld.setDefaultValue(feldDetails.getDefaultValue());
                    newFeld.setMinLength(feldDetails.getMinLength());
                    newFeld.setMaxLength(feldDetails.getMaxLength());
                    newFeld.setRegexPattern(feldDetails.getRegexPattern());
                    newFeld.setOptionen(feldDetails.getOptionen());
                    newFeld.setCheckboxLabelTrue(feldDetails.getCheckboxLabelTrue());
                    newFeld.setCheckboxLabelFalse(feldDetails.getCheckboxLabelFalse());
                    newFeld.setFileTypes(feldDetails.getFileTypes());
                    newFeld.setMaxFileSize(feldDetails.getMaxFileSize());
                    newFeld.setMinValue(feldDetails.getMinValue());
                    newFeld.setMaxValue(feldDetails.getMaxValue());
                    newFeld.setFormular(formular);
                    formular.addFeld(newFeld);
                }
            }
        }

        return formularRepository.save(formular);
    }

    // Update der veroeffentlichenFormular Methode für Status
    public Formular veroeffentlichenFormular(Long id) {
        Formular formular = getFormularById(id);
        formular.setStatus(FormularStatus.PUBLISHED); // Statt Boolean
        formular.setIstVeroeffentlicht(true); // Für Abwärtskompatibilität
        log.info("Formular veröffentlicht: {}", id);
        return formularRepository.save(formular);
    }

    public Formular zurueckziehenFormular(Long id) {
        Formular formular = getFormularById(id);
        formular.setStatus(FormularStatus.DRAFT); // Zurück zu Entwurf
        formular.setIstVeroeffentlicht(false); // Für Abwärtskompatibilität
        log.info("Formular zurückgezogen: {}", id);
        return formularRepository.save(formular);
    }
    public Formular archivierenFormular(Long id) {
        Formular formular = getFormularById(id);
        formular.setStatus(FormularStatus.ARCHIVED); // Neuer Status
        log.info("Formular archiviert: {}", id);
        return formularRepository.save(formular);
    }


    public void deleteFormular(Long id) {
        formularRepository.deleteById(id);
        log.info("Formular gelöscht: {}", id);
    }
}