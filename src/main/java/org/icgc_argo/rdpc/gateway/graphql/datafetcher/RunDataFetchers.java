package org.icgc_argo.rdpc.gateway.graphql.datafetcher;

import graphql.schema.DataFetcher;
import java.util.List;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor // TODO: Remove when implemented
public class RunDataFetchers {

  @SneakyThrows
  public DataFetcher<List<?>> getRunsDataFetcher() {
    return environment -> {
      throw new ExecutionControl.NotImplementedException("Not Implemented");
    };
  }
}
