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

        Set<Group> templates = phrases.templates();


        Group expectedValues = new Group("this costs around (\\d+)$", Set.copyOf(allValues));


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

        Set<Group> templates = phrases.templates();


        Group group1 = new Group("this costs around (\\d+)$", Set.of(
                "this costs around 20$",
                "this costs around 30$",
                "this costs around 40$",
                "this costs around 50$",
                "this costs around 60$"));

        Group group2 = new Group("we just paid you (\\d+)$ for this", Set.of(
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


        Set<Group> templates = phrases.templates();


        Group expected = new Group("Hello (\\w+) how was your day?", Set.copyOf(similarButWithOutNumbers));


        assertEquals(Set.of(expected), templates);


    }

    @Test
    @DisplayName("Given a list of Strings with common values, should group then with a template")
    void noNumbersCase2() {


        List<String> similarButWithOutNumbers = List.of(
                """
                Labs for COVID-19 testing will be covered.
                Call your provider beforehand to ask about the safest way to travel,
                where to wait to be seen,
                and whether testing is available.
                Your medical plan doesn't cover any at-home COVID-19 tests.
                You may or may not have coverage through your pharmacy plan.
                You can check your pharmacy plan's benefits by logging into your Prime Therapeutics account or by contacting a Collective Health Member Advocate.
                """,
                """
                Labs for COVID-19 testing will be covered.
                Call your provider beforehand to ask about the safest way to travel,
                where to wait to be seen,
                and whether testing is available.
                Your medical plan doesn't cover any at-home COVID-19 tests.
                You may or may not have coverage through your pharmacy plan.
                You can check your pharmacy plan's benefits by logging into your Express Scripts account or by contacting a Collective Health Member Advocate.
                """
        );


        Phrases phrases = new Phrases(similarButWithOutNumbers);


        Set<Group> templates = phrases.templates();


        Group expected = new Group(
                        """
                        Labs for COVID-(\\d+) testing will be covered.
                        Call your provider beforehand to ask about the safest way to travel,
                        where to wait to be seen,
                        and whether testing is available.
                        Your medical plan doesn't cover any at-home COVID-(\\d+) tests.
                        You may or may not have coverage through your pharmacy plan.
                        You can check your pharmacy plan's benefits by logging into your (\\w+) (\\w+) account or by contacting a Collective Health Member Advocate.""", Set.copyOf(similarButWithOutNumbers));


        assertEquals(Set.of(expected), templates);
    }
}