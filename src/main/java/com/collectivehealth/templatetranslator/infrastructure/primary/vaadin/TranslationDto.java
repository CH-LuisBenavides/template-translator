package com.collectivehealth.templatetranslator.infrastructure.primary.vaadin;

import com.collectivehealth.templatetranslator.domain.Translation;

record TranslationDto(
        String translatedTemplate,
        GroupDto group
) {

    static TranslationDto fromDomain(Translation translation){
        return new TranslationDto(
                translation.translatedTemplate(),
                GroupDto.from(translation.group()));
    }



}
