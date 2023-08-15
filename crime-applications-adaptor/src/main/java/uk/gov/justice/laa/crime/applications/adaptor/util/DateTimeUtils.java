package uk.gov.justice.laa.crime.applications.adaptor.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class DateTimeUtils {

    public static Date toDate(LocalDateTime source) {
        if (source != null) {
            return Date.from(
                    source.atZone(ZoneId.systemDefault())
                            .toInstant());
        } else {
            return null;
        }
    }

    public static String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return df.format(date);
    }
}