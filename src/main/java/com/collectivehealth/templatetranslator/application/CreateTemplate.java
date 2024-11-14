package com.collectivehealth.templatetranslator.application;

import com.collectivehealth.templatetranslator.application.secondary.DataSource;
import com.collectivehealth.templatetranslator.application.secondary.SourceInfo;
import com.collectivehealth.templatetranslator.domain.Group;
import com.collectivehealth.templatetranslator.domain.Phrases;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CreateTemplate {

    private final DataSource dataSource;

    public CreateTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Set<Group> handle(SourceInfo sourceInfo){
        Phrases phrases = dataSource.fetchPhrases(sourceInfo);

        return phrases.templates();
    }


}
