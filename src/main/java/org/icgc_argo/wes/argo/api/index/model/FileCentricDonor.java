package org.icgc_argo.wes.argo.api.index.model;

import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class FileCentricDonor {
  @NonNull private String id;

  @NonNull private String submittedId;

  @NonNull private Specimen specimen;
}
