package com.collectivehealth.templatetranslator.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public record Translation(String translatedTemplate, Group group) {


    public record TranslatedGroup(Set<String> original, Set<String> translated) {

        @Override
        public String toString() {
            return """
                    {
                        "original": %s,
                        "translated": %s
                    }
                    """.formatted(original, translated);
        }
    }


    public TranslatedGroup translate(){

        Set<String> originalValues = group.phrases();



        Set<String> translated = group.phrases()
                .stream()
                .map(this::replacePlaceholders)
                .collect(Collectors.toSet());


        return new TranslatedGroup(originalValues, translated);
    }

    private  String replacePlaceholders(String text) {
        Pattern pattern = Pattern.compile(group.template().replaceAll("\\?", "\\?").replaceAll("\\$","\\\\\\$"));
        Matcher matcher = pattern.matcher(text.trim());

        if (matcher.matches()) {
            // Capture groups dynamically into a list
            List<String> capturedValues = new ArrayList<>();
            int groupCount = matcher.groupCount();
            for (int i = 1; i <= groupCount; i++) {
                capturedValues.add(matcher.group(i));
            }

            // Log the captured values
            System.out.println("Captured Values: " + capturedValues);

            // Replace placeholders in the template
            String translatedText = translatedTemplate;
            int groupIndex = 1;

            for (String value : capturedValues) {
                // Replace the first occurrence of the regex pattern placeholder dynamically
                translatedText = translatedText.replaceFirst("\\(\\\\[wd+]{0,2}\\)", value);
                groupIndex++;
            }

            return translatedText;
        } else {
            return "No match found for text: " + text;
        }
    }


}
