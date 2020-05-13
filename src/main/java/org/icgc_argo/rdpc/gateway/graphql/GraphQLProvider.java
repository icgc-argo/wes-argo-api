package org.icgc_argo.rdpc.gateway.graphql;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.val;
import org.icgc_argo.rdpc.gateway.graphql.datafetcher.RunDataFetchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GraphQLProvider {

  /** State */
  private GraphQL graphQL;

  /** Dependencies */
  private final RunDataFetchers runDataFetchers;

  @Autowired
  public GraphQLProvider(RunDataFetchers runDataFetchers) {
    this.runDataFetchers = runDataFetchers;
  }

  @Bean
  public GraphQL graphql() {
    return graphQL;
  }

  @PostConstruct
  public void init() throws IOException {
    val url = Resources.getResource("schema.graphqls");
    String sdl = Resources.toString(url, Charsets.UTF_8);
    GraphQLSchema graphQLSchema = buildSchema(sdl);
    this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
  }

  private GraphQLSchema buildSchema(String sdl) {
    val typeRegistry = new SchemaParser().parse(sdl);
    RuntimeWiring runtimeWiring = buildWiring();
    val schemaGenerator = new SchemaGenerator();
    return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
  }

  private RuntimeWiring buildWiring() {
    return RuntimeWiring.newRuntimeWiring()
        .scalar(ExtendedScalars.Json)
        .type(newTypeWiring("Query").dataFetcher("runs", runDataFetchers.getRunsDataFetcher()))
        .build();
  }
}
