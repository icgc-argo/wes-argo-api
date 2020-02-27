package org.icgc_argo.wes.argo.api.index.model;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Sample {
  @NonNull private String id;
  @NonNull private String submittedId;
  @NonNull private String type;
  private String matchedNormalSubmitterSampleId;
}
