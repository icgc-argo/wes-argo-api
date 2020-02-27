package org.icgc_argo.wes.argo.api.fetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc_argo.wes.argo.api.graphql.model.Analysis;
import org.icgc_argo.wes.argo.api.service.ArgoService;
import org.icgc_argo.wes.argo.api.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.icgc_argo.wes.argo.api.util.Converter.buildAnalysis;

@Slf4j
@Component
public class AnalysisDataFetcher implements DataFetcher<Analysis> {

  private ArgoService argoService;

  @Autowired
  public AnalysisDataFetcher(@NonNull ArgoService argoService) {
    this.argoService = argoService;
  }

  @Override
  public Analysis get(DataFetchingEnvironment environment) throws Exception {
    String id = environment.getArgument("analysisId");
    val doc = argoService.getFileCentricDocumentByAnalysisId(id).get();
    return buildAnalysis(doc);
  }
}
