package org.icgc_argo.wes.argo.api.util;

import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.icgc_argo.wes.argo.api.graphql.model.Analysis;
import org.icgc_argo.wes.argo.api.graphql.model.Donor;
import org.icgc_argo.wes.argo.api.index.model.FileCentricDocument;
import org.icgc_argo.wes.argo.api.index.model.FileCentricDonor;

@Slf4j
@UtilityClass
public class Converter {

  public static Analysis buildAnalysis(@NonNull FileCentricDocument doc) {
    return Analysis.builder()
        .analysisId(doc.getAnalysis().getId())
        .analysisTypeName(doc.getAnalysis().getType().getName())
        .analysisTypeVersion(doc.getAnalysis().getType().getVersion())
        .donors(
            doc.getDonors().stream()
                .map(
                    donor -> {
                      return buildDonor(donor);
                    })
                .collect(Collectors.toList()))
        .build();
  }

  public static Donor buildDonor(@NonNull FileCentricDonor source) {
    return Donor.builder()
            .donorId(source.getId())
            .submittedId(source.getSubmittedId())
            .build();
  }
}
