package uk.gov.justice.laa.crime.applications.adaptor.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class DateTimeUtils {

    private static final DateTimeFormatter START_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static LocalDateTime toLocalDateTime(Date source) {
        if (source != null) {
            return source.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } else {
            return null;
        }
    }

    public static Date toDate(LocalDateTime source) {
        if (source != null) {
            return Date.from(
                    source.atZone(ZoneId.systemDefault())
                            .toInstant());
        } else {
            return null;
        }
    }

    public static Timestamp toTimeStamp(LocalDateTime source) {
        if (source != null) {
            return Timestamp.valueOf(source);
        } else {
            return null;
        }
    }

    public static String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return df.format(date);
    }
}