package uk.gov.justice.laa.crime.applications.adaptor.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class DateTimeUtils {

  public static Date toDate(LocalDateTime source) {
    if (source != null) {
      return Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
    }
    return null;
  }

  public static String dateToString(Date date) {
    if (date != null) {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
      return df.format(date);
    }
    return null;
  }
}
