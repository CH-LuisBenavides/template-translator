package com.collectivehealth.templatetranslator.domain;

import java.util.*;
import java.util.stream.Collectors;

public class Phrases {

    private final List<String> all;

    public Phrases(List<String> all) {
        this.all = all.stream().sorted().toList();
    }

    public static class Group {
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
                    """.formatted(template, phrases.stream().sorted().collect(Collectors.joining(",\n","[\n","]\n")));
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

    public Set<Group> templates() {
        List<Group> groups = new ArrayList<>();

        for (String phrase : all) {
            String sanitizedPhrase = replaceNumbers(phrase);
            String[] tokens = sanitizedPhrase.split(" ");
            Group group = findOrCreateGroup(tokens, groups);
            group.addPhrase(phrase);
        }

        return new HashSet<>(groups);
    }

    private String replaceNumbers(String phrase) {
        return phrase.replaceAll("\\d+", "{NUM}");
    }

    private Group findOrCreateGroup(String[] tokens, List<Group> groups) {
        String template = buildTemplate(tokens);
        return groups.stream()
                .filter(g -> g.template().equals(template))
                .findFirst()
                .orElseGet(() -> createGroup(template, groups));
    }

    private Group createGroup(String template, List<Group> groups) {
        Group newGroup = new Group(template);
        groups.add(newGroup);
        return newGroup;
    }

    private String buildTemplate(String[] tokens) {
        boolean[] isVariable = new boolean[tokens.length];

        for (String phrase : all) {
            String sanitizedPhrase = replaceNumbers(phrase);
            String[] currentTokens = sanitizedPhrase.split(" ");

            if(tokens.length != currentTokens.length) continue;

            for (int i = 0; i < tokens.length; i++) {
                if (!tokens[i].equals(currentTokens[i])) {
                    isVariable[i] = true;
                }
            }
        }
        int numCounter = 1;
        int wordCounter = 1;
        StringBuilder templateBuilder = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            if (isVariable[i]) {
                templateBuilder.append("{WORD%d} ".formatted(wordCounter));
                wordCounter++;

            } else {
                if (tokens[i].contains("{NUM}")) {
                    templateBuilder.append(tokens[i].replace("{NUM}","{NUM%d}").formatted(numCounter)).append(" ") ;
                    numCounter++;
                } else {

                templateBuilder.append(tokens[i]).append(" ");
                }
            }
        }

        return templateBuilder.toString().trim();
    }
}