package org.icgc_argo.wes.argo.api.index.model;

import java.util.Map;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldNameConstants
public class FileCentricAnalysis {
  @NonNull private String id;
  @NonNull private AnalysisType type;
  @NonNull private String state;
  @NonNull private String studyId;
  @NonNull private Map<String, Object> experiment;
}
