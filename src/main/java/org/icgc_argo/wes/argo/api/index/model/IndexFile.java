package org.icgc_argo.wes.argo.api.index.model;

import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IndexFile {
  @NonNull private String objectId;
  @NonNull private String name;
  @NonNull private String format;
  @NonNull private String md5sum;
  private String dataType;
  private Long size;
}
