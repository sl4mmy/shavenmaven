package com.googlecode.shavenmaven.config;

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.predicates.Predicate;

import java.io.*;
import java.util.Properties;

import static com.googlecode.totallylazy.Files.asFile;
import static com.googlecode.totallylazy.Files.fileOption;
import static com.googlecode.totallylazy.Maps.get;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Properties.properties;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.lines;
import static com.googlecode.totallylazy.Strings.startsWith;
import static java.lang.System.getProperty;

public class SectionedProperties {
    private final String content;

    private SectionedProperties(String content) {
        this.content = content;
    }

    public static SectionedProperties sectionedProperties(String content) {
        return new SectionedProperties(content);
    }

    public static SectionedProperties sectionedProperties() {
        return sectionedProperties(option(getProperty("smrcLocation")).map(asFile()).
                orElse(fileOption(new File(getProperty("user.home")), ".smrc")).map(Strings::string).
                getOrElse(""));
    }

    public Option<Properties> section(String section) {
        return get(map(lines(new StringReader(content)).recursive(splitSections(startsWith("["))).map(this::toProperties)), section);
    }

    private Pair<String,Properties> toProperties(Sequence<String> s) throws IOException {
        return pair(s.first().replace("[", "").replace("]", ""), properties(inputStream(s.tail())));
    }

    private InputStream inputStream(Sequence<String> strings) {
        return new ByteArrayInputStream(strings.toString("\n").getBytes());
    }

    private Function1<Sequence<String>, Pair<Sequence<String>, Sequence<String>>> splitSections(final Predicate<String> predicate) {
        return strings -> {
            final Pair<Sequence<String>, Sequence<String>> split = strings.breakOn(predicate);
            final Sequence<String> section = split.second();
            if(section.isEmpty()) {
                return pair(empty(String.class), empty(String.class));
            }
            final Pair<Sequence<String>, Sequence<String>> sectionAndRest = section.drop(1).breakOn(predicate);
            final String first = section.first();
            final Sequence<String> first1 = sectionAndRest.first();
            return pair(sequence(first).join(first1), sectionAndRest.second());
        };
    }
}
