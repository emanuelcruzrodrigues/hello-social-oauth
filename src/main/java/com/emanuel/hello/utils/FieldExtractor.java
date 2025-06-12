package com.emanuel.hello.utils;

import com.emanuel.hello.domain.Person;

import java.util.Map;

public class FieldExtractor {


    public static String extractSubjectId(Map<String, Object> attributes) {
        String subjectId = (String) attributes.get("sub");
        if (subjectId == null) {
            subjectId = attributes.get("id").toString();
        }
        return subjectId;
    }

    public static String extractEmail(Map<String, Object> attributes) {
        return (String) attributes.get("email");
    }

    public static String extractName(Map<String, Object> attributes, Person person) {
        String name = (String) attributes.get("name");
        person.setName(name);
        return name;
    }

    public static String extractGivenName(Map<String, Object> attributes, String name) {
        String givenName = (String) attributes.get("given_name");
        if (givenName == null) {
            givenName = name.split(" ")[0].trim();
        }
        return givenName;
    }

    public static String extractFamilyName(Map<String, Object> attributes, String name, String givenName) {
        String familyName = (String) attributes.get("family_name");
        if (familyName == null) {
            familyName = name.replace(givenName, "").trim();
        }
        return familyName;
    }
}
