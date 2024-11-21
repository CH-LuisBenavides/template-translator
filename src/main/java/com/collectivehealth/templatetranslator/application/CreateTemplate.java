package com.collectivehealth.templatetranslator.application;

import com.collectivehealth.templatetranslator.application.secondary.DataSource;
import com.collectivehealth.templatetranslator.application.secondary.SourceInfo;
import com.collectivehealth.templatetranslator.application.secondary.TemplateRepository;
import com.collectivehealth.templatetranslator.domain.Phrases;
import com.collectivehealth.templatetranslator.domain.Translation;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CreateTemplate {

    private final DataSource dataSource;
    private final TemplateRepository templateRepository;

    public CreateTemplate(DataSource dataSource, TemplateRepository templateRepository) {
        this.dataSource = dataSource;
        this.templateRepository = templateRepository;
    }

    public Set<Translation> handle(SourceInfo sourceInfo){
        Phrases phrases = dataSource.fetchPhrases(sourceInfo);

        return templateRepository.findTranslation(phrases.templates());
    }


}
