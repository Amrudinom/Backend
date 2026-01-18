package com.dke.foerderportal.shared.service;

import com.dke.foerderportal.shared.model.Formular;
import com.dke.foerderportal.shared.model.Formularfeld;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FormularSnapshotBuilder {

    private final ObjectMapper objectMapper;

    public JsonNode buildSnapshot(Formular formular) {
        ObjectNode root = objectMapper.createObjectNode();

        root.put("id", formular.getId());
        root.put("titel", formular.getTitel());
        root.put("beschreibung", formular.getBeschreibung());
        root.put("kategorie", formular.getKategorie());
        root.put("version", formular.getVersion());

        ArrayNode felderArray = root.putArray("felder");

        formular.getFelder().stream()
                .sorted(Comparator.comparing(Formularfeld::getAnzeigeReihenfolge))
                .forEach(f -> {
                    ObjectNode n = felderArray.addObject();

                    n.put("id", f.getId());
                    n.put("feldName", f.getFeldName());
                    n.put("feldTyp", f.getFeldTyp().name());
                    n.put("label", f.getLabel());
                    n.put("pflichtfeld", f.isPflichtfeld());

                    n.put("oauthAutoFill", Boolean.TRUE.equals(f.getOauthAutoFill()));
                    n.put("oauthFieldMapping", f.getOauthFieldMapping());

                    n.put("anzeigeReihenfolge", f.getAnzeigeReihenfolge());

                    if (f.getPlaceholder() != null) n.put("placeholder", f.getPlaceholder());
                    if (f.getDefaultValue() != null) n.put("defaultValue", f.getDefaultValue());
                    if (f.getMinLength() != null) n.put("minLength", f.getMinLength());
                    if (f.getMaxLength() != null) n.put("maxLength", f.getMaxLength());
                    if (f.getRegexPattern() != null) n.put("regexPattern", f.getRegexPattern());

                    if (f.getOptionen() != null) n.set("optionen", f.getOptionen());

                    if (f.getCheckboxLabelTrue() != null) n.put("checkboxLabelTrue", f.getCheckboxLabelTrue());
                    if (f.getCheckboxLabelFalse() != null) n.put("checkboxLabelFalse", f.getCheckboxLabelFalse());

                    if (f.getFileTypes() != null) n.put("fileTypes", f.getFileTypes());
                    if (f.getMaxFileSize() != null) n.put("maxFileSize", f.getMaxFileSize());

                    if (f.getMinValue() != null) n.put("minValue", f.getMinValue());
                    if (f.getMaxValue() != null) n.put("maxValue", f.getMaxValue());
                });

        return root;
    }

    public JsonNode answersToJson(Map<String, Object> antworten) {
        return objectMapper.valueToTree(antworten);
    }
}
