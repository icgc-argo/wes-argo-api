package org.icgc_argo.wes.argo.api.fetchers;

import static org.icgc_argo.wes.argo.api.util.Converter.buildDonor;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc_argo.wes.argo.api.exceptions.NotFoundException;
import org.icgc_argo.wes.argo.api.graphql.model.Donor;
import org.icgc_argo.wes.argo.api.service.ArgoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DonorDataFetcher implements DataFetcher<Donor> {

  private ArgoService argoService;

  @Autowired
  public DonorDataFetcher(@NonNull ArgoService argoService) {
    this.argoService = argoService;
  }

  @Override
  public Donor get(DataFetchingEnvironment environment) throws Exception {
    String donorId = environment.getArgument("donorId");
    val doc = argoService.getFileCentricDocumentByDonorId(donorId).get();
    NotFoundException.checkNotFound(
        doc.getDonors().size() > 0, String.format("No donor found for donor id = %s", donorId));
    val fileCentricDonor = doc.getDonors().get(0);
    return buildDonor(fileCentricDonor);
  }
}
