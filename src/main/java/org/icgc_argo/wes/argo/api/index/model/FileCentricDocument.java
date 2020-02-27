package org.icgc_argo.wes.argo.api.index.model;

import java.util.List;
import lombok.*;
import lombok.experimental.FieldNameConstants;

/** ES index file_centric document java model. */
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldNameConstants
public class FileCentricDocument {
  @NonNull private String objectId;

  @NonNull private String access;

  @NonNull private String studyId;

  @NonNull private FileCentricAnalysis analysis;

  /** The actual genome analysis files information. */
  @NonNull private File files;

  /**
   * Each files can be hosted in more than one files repository, this references the other
   * repositories (locations) where this files can be fetched from.
   */
  @NonNull private List<Repository> repositories;

  @NonNull private List<FileCentricDonor> donors;
}
