package com.collectivehealth.templatetranslator.application;

import com.collectivehealth.templatetranslator.application.secondary.TemplateRepository;
import com.collectivehealth.templatetranslator.domain.Translation;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TranslateData {

    private final TemplateRepository repository;

    public TranslateData(TemplateRepository repository) {
        this.repository = repository;
    }

    public Set<Translation.TranslatedGroup> translations(Set<Translation> translations){
        repository.addTranslationTemplate(translations);

        return translations.stream()
                .map(Translation::translate)
                .collect(Collectors.toSet());
    }


}
