package com.collectivehealth.templatetranslator.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Group {
    private final String template;
    private final Set<String> phrases;

    public Group(String template, Set<String> phrases) {
        this.phrases = phrases;
        this.template = template;
    }

    public Group(String template) {
        this.template = template;
        this.phrases = new HashSet<>();
    }

    public Set<String> phrases() {
        return Set.copyOf(phrases);
    }

    public String template() {
        return template;
    }

    public void addPhrase(String phrase) {
        phrases.add(phrase);
    }

    @Override
    public String toString() {

        return """
                {
                    template: "%s",
                    phrases: %s
                }
                """.formatted(template, phrases.stream()
                .sorted()
                .collect(Collectors.joining(",\n", "[\n", "]\n")));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(template, group.template) && Objects.equals(phrases, group.phrases);
    }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(template, phrases);
//        }
}
