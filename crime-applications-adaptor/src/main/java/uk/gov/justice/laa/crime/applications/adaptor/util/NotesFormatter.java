package uk.gov.justice.laa.crime.applications.adaptor.util;

public class NotesFormatter {
    public static String formatNote(String note) {
        // Capitalise the first letter of the first word
        String capitalise = note.substring(0, 1).toUpperCase() + note.substring(1);
        return capitalise.replaceAll("_", " ");
    }
}
