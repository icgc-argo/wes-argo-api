package org.icgc_argo.wes.argo.api.index.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AnalysisType {
  @NonNull private String name;
  @NonNull private Integer version;
}
