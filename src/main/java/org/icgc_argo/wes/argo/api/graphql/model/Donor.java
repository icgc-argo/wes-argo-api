package org.icgc_argo.wes.argo.api.graphql.model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Donor {
  @NonNull private String donorId;
  @NonNull private String submittedId;
}
