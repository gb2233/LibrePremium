package xyz.kyngs.librepremium.common.util;

public class EmailMasker {
    public static String mask(String email){
        String id = email.substring(0, email.lastIndexOf("@"));
        String domain = email.substring(email.lastIndexOf("@"));
        switch (id.length()) {
            case 0, 1 -> id = "*";
            case 2 -> id = id.charAt(0) + "*";
            case 3 -> id = id.charAt(0) + "*" + id.substring(2);
            case 4 -> id = id.charAt(0) + "**" + id.substring(3);
            default -> {
                String masks = String.join("", java.util.Collections.nCopies(id.length() - 4, "*"));
                id = id.substring(0, 2) + masks + id.substring(id.length() - 2);
            }
        }
        switch (domain.length()) {
            case 0 -> domain = "*";
            case 1,2 -> domain = "@*";
            case 3 -> domain = domain.charAt(0) + "*" + domain.substring(2);
            case 4 -> domain = domain.charAt(0) + "**" + domain.substring(3);
            default -> {
                String masks = String.join("", java.util.Collections.nCopies(domain.length() - 4, "*"));
                domain = domain.substring(0, 2) + masks + domain.substring(domain.length() - 2);
            }
        }

        return id + domain;
    }
}
