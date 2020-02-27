package org.icgc_argo.wes.argo.api;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.IOException;
import java.net.URL;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import org.icgc_argo.wes.argo.api.fetchers.AnalysisDataFetcher;
import org.icgc_argo.wes.argo.api.fetchers.DonorDataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GraphQLProvider {

  private GraphQL graphQL;

  private AnalysisDataFetcher analysisDataFetcher;

  private DonorDataFetcher donorDataFetcher;

  @Bean
  public GraphQL graphQL() {
    return graphQL;
  }

  @Autowired
  public GraphQLProvider(@NonNull AnalysisDataFetcher analysisDataFetcher, @NonNull DonorDataFetcher donorDataFetcher) {
    this.analysisDataFetcher = analysisDataFetcher;
    this.donorDataFetcher = donorDataFetcher;
  }

  @PostConstruct
  public void init() throws IOException {
    URL url = Resources.getResource("schema.graphqls");
    String sdl = Resources.toString(url, Charsets.UTF_8);
    GraphQLSchema graphQLSchema = buildSchema(sdl);
    this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
  }

  private GraphQLSchema buildSchema(String sdl) {
    TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
    RuntimeWiring runtimeWiring = buildWiring();
    SchemaGenerator schemaGenerator = new SchemaGenerator();
    return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
  }

  private RuntimeWiring buildWiring() {
    return RuntimeWiring.newRuntimeWiring()
        .scalar(ExtendedScalars.Json)
        .type(newTypeWiring("Query").dataFetcher("analysisById", analysisDataFetcher))
        .type(newTypeWiring("Query").dataFetcher("donorById", donorDataFetcher))
        .build();
  }
}
