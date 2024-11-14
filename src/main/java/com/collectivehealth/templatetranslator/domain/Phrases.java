package com.collectivehealth.templatetranslator.domain;

import java.util.*;

public class Phrases {

    private final List<String> all;

    public Phrases(List<String> all) {
        this.all = all.stream().sorted().toList();
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
        return phrase.replaceAll("\\d+", "(\\\\d+)");
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
                templateBuilder.append("(\\w+) ");
                wordCounter++;

            } else {
                templateBuilder.append(tokens[i]).append(" ");
            }
        }

        return templateBuilder.toString().trim();
    }
}