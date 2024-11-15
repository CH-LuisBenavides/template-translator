package com.collectivehealth.templatetranslator.domain;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TranslationTest {


    @Test
    void translatingWithOnlyNumbers() {
        Set<String> allValues = Set.of(
                "this costs around 20$",
                "this costs around 30$",
                "this costs around 40$",
                "this costs around 50$",
                "this costs around 60$"
        );

        Group group = new Group("this costs around (\\d+)$", Set.copyOf(allValues));


        Translation.TranslatedGroup translate = new Translation("Esto cuesta alrededor de (\\d+)$", group).translate();

        Translation.TranslatedGroup expected = new Translation.TranslatedGroup(allValues, Set.of(

                "Esto cuesta alrededor de 20$",
                "Esto cuesta alrededor de 30$",
                "Esto cuesta alrededor de 40$",
                "Esto cuesta alrededor de 50$",
                "Esto cuesta alrededor de 60$"

        ));

        assertEquals(expected, translate);

    }

    @Test
    void translatingWithOnlyWords() {
        Set<String> originalValues = Set.of(
                "hello there Luis Benavides, how was your day?",
                "hello there Andrea Larreal, how was your day?",
                "hello there Jesus Benavides, how was your day?",
                "hello there David Benavides, how was your day?"
        );
        Group group = new Group("hello there (\\w+) (\\w+), how was your day?", originalValues);

        Translation translation = new Translation("Hola (\\w+) (\\w+), como estuvo tu dia?", group);


        Translation.TranslatedGroup translations = translation.translate();


        Translation.TranslatedGroup expected = new Translation.TranslatedGroup(originalValues, Set.of(
                "Hola Luis Benavides, como estuvo tu dia?",
                "Hola Andrea Larreal, como estuvo tu dia?",
                "Hola Jesus Benavides, como estuvo tu dia?",
                "Hola David Benavides, como estuvo tu dia?"
        ));

        assertEquals(expected, translations);

    }


    @Test
    void translatingWithMoreThanOneNumberAndWord() {
        Set<String> similarButWithOutNumbers = Set.of(
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



        Group group = new Group(
                """
                Labs for COVID-(\\d+) testing will be covered.
                Call your provider beforehand to ask about the safest way to travel,
                where to wait to be seen,
                and whether testing is available.
                Your medical plan doesn't cover any at-home COVID-(\\d+) tests.
                You may or may not have coverage through your pharmacy plan.
                You can check your pharmacy plan's benefits by logging into your (\\w+) (\\w+) account or by contacting a Collective Health Member Advocate.""", similarButWithOutNumbers);


        Translation translation = new Translation("El Covid-(\\d+) es complejo por loque para evitar el Covid-(\\d+), deberiamos contactar a (\\w+) (\\w+)", group);


        Translation.TranslatedGroup translate = translation.translate();

        Translation.TranslatedGroup expected = new Translation.TranslatedGroup(similarButWithOutNumbers,
                Set.of("El Covid-19 es complejo por loque para evitar el Covid-19, deberiamos contactar a Prime Therapeutics", "El Covid-19 es complejo por loque para evitar el Covid-19, deberiamos contactar a Express Scripts")
        );


        assertEquals(expected, translate);
    }
    @Test
    void translatingWithMoreThanOneNumberAndWord2() {
        Set<String> similarButWithOutNumbers = Set.of(
                        "Want to integrate acupuncture into your care routine? This plan covers up to 20 sessions per year with a licensed acupuncturist.",
                        "Want to integrate acupuncture into your care routine? This plan covers up to 12 sessions per year with a licensed acupuncturist.",
                        "Want to integrate acupuncture into your care routine? This plan covers up to 10 sessions per year with a licensed acupuncturist.",
                        "Want to integrate acupuncture into your care routine? This plan covers up to 24 sessions per year with a licensed acupuncturist.",
                        "Want to integrate acupuncture into your care routine? This plan covers up to 30 sessions per year with a licensed acupuncturist."
        );



        Group group = new Group(
                "Want to integrate acupuncture into your care routine? This plan covers up to (\\d+) sessions per year with a licensed acupuncturist.", similarButWithOutNumbers);


        Translation translation = new Translation("¿Quieres integrar la acupuntura en tu rutina de cuidado? Este plan cubre hasta (\\d+) sesiones por año con un acupunturista licenciado.", group);


        Translation.TranslatedGroup translate = translation.translate();

        Translation.TranslatedGroup expected = new Translation.TranslatedGroup(similarButWithOutNumbers,
                Set.of(
                        "¿Quieres integrar la acupuntura en tu rutina de cuidado? Este plan cubre hasta 20 sesiones por año con un acupunturista licenciado.",
                        "¿Quieres integrar la acupuntura en tu rutina de cuidado? Este plan cubre hasta 12 sesiones por año con un acupunturista licenciado.",
                        "¿Quieres integrar la acupuntura en tu rutina de cuidado? Este plan cubre hasta 10 sesiones por año con un acupunturista licenciado.",
                        "¿Quieres integrar la acupuntura en tu rutina de cuidado? Este plan cubre hasta 24 sesiones por año con un acupunturista licenciado.",
                        "¿Quieres integrar la acupuntura en tu rutina de cuidado? Este plan cubre hasta 30 sesiones por año con un acupunturista licenciado."
                )
        );


        assertEquals(expected, translate);
    }




}