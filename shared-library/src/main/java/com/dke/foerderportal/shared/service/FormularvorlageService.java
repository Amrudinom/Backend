package com.dke.foerderportal.shared.service;

import com.dke.foerderportal.shared.model.Formularvorlage;
import com.dke.foerderportal.shared.model.User;
import com.dke.foerderportal.shared.repository.FormularvorlageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FormularvorlageService {

    private final FormularvorlageRepository formularvorlageRepository;

    public List<Formularvorlage> getAllVorlagen() {
        return formularvorlageRepository.findAll();
    }

    public List<Formularvorlage> getVeroeffentlichteVorlagen() {
        return formularvorlageRepository.findByIstVeroeffentlichtTrue();
    }

    public Formularvorlage getVorlageById(Long id) {
        return formularvorlageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vorlage nicht gefunden: " + id));
    }

    public Formularvorlage createVorlage(Formularvorlage vorlage, User ersteller) {
        vorlage.setErstellerVon(ersteller);
        vorlage.setIstVeroeffentlicht(false);

        Formularvorlage saved = formularvorlageRepository.save(vorlage);
        log.info("Formularvorlage erstellt: {}", saved.getId());
        return saved;
    }

    public Formularvorlage updateVorlage(Long id, Formularvorlage vorlageDetails) {
        Formularvorlage vorlage = getVorlageById(id);

        vorlage.setTitel(vorlageDetails.getTitel());
        vorlage.setBeschreibung(vorlageDetails.getBeschreibung());
        vorlage.setKategorie(vorlageDetails.getKategorie());

        return formularvorlageRepository.save(vorlage);
    }

    public void deleteVorlage(Long id) {
        formularvorlageRepository.deleteById(id);
        log.info("Formularvorlage gelöscht: {}", id);
    }
}