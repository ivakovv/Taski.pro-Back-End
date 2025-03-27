package com.project.taskipro.model.user.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum EmailDomains {
    YANDEX_RU("yandex\\.ru"),
    MAIL_RU("mail\\.ru"),
    GMAIL_COM("gmail\\.com"),
    YAHOO_COM("yahoo\\.com"),
    OUTLOOK_COM("outlook\\.com");

    private final String domain;

    public static String getRegexForDomains() {
        return Stream.of(EmailDomains.values())
                .map(EmailDomains::getDomain)
                .collect(Collectors.joining("|"));
    }
}
