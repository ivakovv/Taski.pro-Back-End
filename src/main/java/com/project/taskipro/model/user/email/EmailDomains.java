package com.project.taskipro.model.user.email;

public enum EmailDomains {
    YANDEX_RU("yandex\\.ru"),
    MAIL_RU("mail\\.ru"),
    GMAIL_COM("gmail\\.com"),
    YAHOO_COM("yahoo\\.com"),
    OUTLOOK_COM("outlook\\.com");

    private final String domain;

    EmailDomains(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public static String getRegexForDomains() {
        StringBuilder regex = new StringBuilder();
        for (EmailDomains emailDomain : EmailDomains.values()) {
            if (!regex.isEmpty()) {
                regex.append("|");
            }
            regex.append(emailDomain.getDomain());
        }
        return regex.toString();
    }
}
