package com.collectivehealth.templatetranslator.infrastructure.secondary.demo;

import com.collectivehealth.templatetranslator.application.secondary.SourceInfo;

import java.util.List;

public record DemoValues(List<String> values) implements SourceInfo {
}
