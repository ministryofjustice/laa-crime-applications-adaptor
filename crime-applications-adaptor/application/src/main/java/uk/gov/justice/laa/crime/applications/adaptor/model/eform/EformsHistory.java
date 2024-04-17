package uk.gov.justice.laa.crime.applications.adaptor.model.eform;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EformsHistory {
  private Integer id;
  private Integer usn;
  private Integer repId;
  private String action;
  private Integer keyId;
  private LocalDateTime dateCreated;
  private String userCreated;
}
