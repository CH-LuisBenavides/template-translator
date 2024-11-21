package com.collectivehealth.templatetranslator.infrastructure.secondary.memory;

import com.collectivehealth.templatetranslator.application.secondary.TemplateRepository;
import com.collectivehealth.templatetranslator.domain.Group;
import com.collectivehealth.templatetranslator.domain.Translation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Slf4j
class FileRepository implements TemplateRepository {

    private final FileProperties fileProperties;

    private final ObjectMapper mapper;

    FileRepository(FileProperties fileProperties, ObjectMapper mapper) {
        this.fileProperties = fileProperties;
        this.mapper = mapper;
    }

    @Override
    public void addTranslationTemplate(Set<Translation> translations) {

        List<TranslationDto> list = translations.stream()
                .map(TranslationDto::fromDomain)
                .toList();

        try {
            File file = fileProperties.file();
            mapper.writeValue(file, list);
        } catch (IOException e) {
            log.error("We could not store the template due to ",e);
        }
    }

    @Override
    public Set<Translation> findTranslation(Set<Group> groups) {
        Set<TranslationDto> translations = loadTranslationsFromFile();


        return groups.stream()
                .map(g -> translations.stream()
                        .filter(t -> t.originalTemplate()
                                .equals(g.template()))
                        .findFirst()
                        .map(t -> new Translation(t.translatedTemplate(), g))
                        .orElse(new Translation("", g)))
                .collect(Collectors.toSet());
    }

    private Set<TranslationDto> loadTranslationsFromFile() {
        try {
            File file = fileProperties.file();
            if (file.exists()) {
                TranslationDto[] translations = mapper.readValue(file, TranslationDto[].class);
                return Set.of(translations);
            } else {
                log.warn("Translation file does not exist: {}", file.getPath());
                return Set.of();
            }
        } catch (IOException e) {
            log.error("Failed to load translations from file", e);
            return Set.of();
        }
    }


    private record TranslationDto(String originalTemplate,String translatedTemplate){
        private static TranslationDto fromDomain(Translation t) {
            return new TranslationDto(t.group().template(), t.translatedTemplate());
        }
    }
}

