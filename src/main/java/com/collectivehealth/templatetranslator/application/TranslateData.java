package com.collectivehealth.templatetranslator.application;

import com.collectivehealth.templatetranslator.domain.Translation;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TranslateData {


    public Set<Translation.TranslatedGroup> translations(Set<Translation> translations){
        return translations.stream().map(Translation::translate).collect(Collectors.toSet());
    }


}
