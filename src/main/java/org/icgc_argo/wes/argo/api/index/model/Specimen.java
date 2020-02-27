package org.icgc_argo.wes.argo.api.index.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Specimen {

  @NonNull private String id;
  @NonNull private String type;
  @NonNull private String submittedId;
  @NonNull private Sample samples;
  private String tumourNormalDesignation;
  private String specimenTissueSource;
}
