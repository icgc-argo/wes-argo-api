package org.icgc_argo.wes.argo.api.index.model;

import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class File {
  @NonNull private String name;
  @NonNull private String format;
  @NonNull private String md5sum;
  private Long size;
  private Long lastModified;
  private String dataType;
  private IndexFile indexFile;
}
