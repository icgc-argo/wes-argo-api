package org.icgc_argo.wes.argo.api.index.model;

import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Repository {
  @NonNull private String code;

  @NonNull private String organization;

  private String name;

  @NonNull private String type;

  @NonNull private String country;

  @NonNull private String baseUrl;

  @NonNull private String dataPath;

  @NonNull private String metadataPath;
}
