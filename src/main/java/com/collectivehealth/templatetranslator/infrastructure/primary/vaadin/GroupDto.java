package com.collectivehealth.templatetranslator.infrastructure.primary.vaadin;

import com.collectivehealth.templatetranslator.domain.Group;

import java.util.Set;

public record GroupDto(String template, Set<String> phrases) {

    public static  GroupDto from(Group group) {
        return new GroupDto(group.template(), group.phrases());
    }

}
