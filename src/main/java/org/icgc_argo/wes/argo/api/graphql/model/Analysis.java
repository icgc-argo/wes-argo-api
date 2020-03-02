package org.icgc_argo.wes.argo.api.graphql.model;

import java.util.List;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Analysis {
  @NonNull private String analysisId;
  @NonNull private Integer analysisTypeVersion;
  @NonNull private String analysisTypeName;
  @NonNull private List<Donor> donors;
}
