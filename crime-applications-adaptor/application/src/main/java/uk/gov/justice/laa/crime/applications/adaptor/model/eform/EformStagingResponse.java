package uk.gov.justice.laa.crime.applications.adaptor.model.eform;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EformStagingResponse {
  private Integer usn;
  private String type;
  private Integer maatRef;
  private String userCreated;
}
