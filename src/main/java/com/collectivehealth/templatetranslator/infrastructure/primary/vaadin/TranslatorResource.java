package com.collectivehealth.templatetranslator.infrastructure.primary.vaadin;

import com.collectivehealth.templatetranslator.application.CreateTemplate;
import com.collectivehealth.templatetranslator.application.TranslateData;
import com.collectivehealth.templatetranslator.domain.Translation;
import com.collectivehealth.templatetranslator.infrastructure.secondary.demo.DemoValues;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import java.util.Set;
import java.util.stream.Collectors;

@BrowserCallable
@AnonymousAllowed
public class TranslatorResource {

    private final CreateTemplate createTemplate;
    private final TranslateData translateData;

    public TranslatorResource(CreateTemplate createTemplate, TranslateData translateData) {
        this.createTemplate = createTemplate;
        this.translateData = translateData;
    }


    public Set<TranslationDto> getTemplates(DemoValues values) {
        return createTemplate.handle(values).stream().map(TranslationDto::fromDomain).collect(Collectors.toSet());
    }

    public Set<Translation.TranslatedGroup> translations(Set<Translation> translations) {
        return translateData.translations(translations);
    }


}
