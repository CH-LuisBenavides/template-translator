package com.collectivehealth.templatetranslator.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PhrasesTest {


    @Test
    @DisplayName("GIven a list of Strings with only numbers of differences should create a template with only the number")
    void givenPhrasesShouldSplitThenByMatch() {
        List<String> allValues = List.of(
                "this costs around 20$",
                "this costs around 30$",
                "this costs around 40$",
                "this costs around 50$",
                "this costs around 60$"
        );
        Phrases phrases = new Phrases(allValues);

        Set<Phrases.Group> templates = phrases.templates();


        Phrases.Group expectedValues = new Phrases.Group("this costs around {NUM}$", Set.copyOf(allValues));


        assertEquals(Set.of(expectedValues), templates);


    }

    @Test
    @DisplayName("Given an unsorted list of Strings with only numbers of differences should create 2 template with only the number")
    void givenPhrasesShouldSplitThenByMatch2() {

        List<String> allValues = List.of(
                "this costs around 20$",
                "this costs around 30$",
                "we just paid you 20$ for this",
                "this costs around 40$",
                "we just paid you 30$ for this",
                "this costs around 50$",
                "we just paid you 40$ for this",
                "we just paid you 50$ for this",
                "this costs around 60$",
                "we just paid you 60$ for this"
        );

        Phrases phrases = new Phrases(allValues);

        Set<Phrases.Group> templates = phrases.templates();


        Phrases.Group group1 = new Phrases.Group("this costs around {NUM}$", Set.of(
                "this costs around 20$",
                "this costs around 30$",
                "this costs around 40$",
                "this costs around 50$",
                "this costs around 60$"));

        Phrases.Group group2 = new Phrases.Group("we just paid you {NUM}$ for this", Set.of(
                "we just paid you 20$ for this",
                "we just paid you 30$ for this",
                "we just paid you 40$ for this",
                "we just paid you 50$ for this",
                "we just paid you 60$ for this"));

        assertEquals(Set.of(group1, group2), templates);

    }

    @Test
    @DisplayName("Given a list of Strings with common values, should group then with a template")
    void noNumbersCase() {


        List<String> similarButWithOutNumbers = List.of(
           "Hello Luis, how was your day?",
           "Hello Andrea, how was your day?",
           "Hello Jesus, how was your day?",
           "Hello David, how was your day?",
           "Hello Zulay, how was your day?"
        );


        Phrases phrases = new Phrases(similarButWithOutNumbers);


        Set<Phrases.Group> templates = phrases.templates();


        Phrases.Group expected = new Phrases.Group("Hello {WORD} how was your day?", Set.copyOf(similarButWithOutNumbers));


        assertEquals(Set.of(expected), templates);


    }


}