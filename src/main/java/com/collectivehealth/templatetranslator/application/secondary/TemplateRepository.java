package com.collectivehealth.templatetranslator.application.secondary;

import com.collectivehealth.templatetranslator.domain.Group;
import com.collectivehealth.templatetranslator.domain.Translation;

import java.util.Set;

public interface TemplateRepository {


    void addTranslationTemplate(Set<Translation> translations);

    Set<Translation> findTranslation(Set<Group> groups);
}
