package org.icgc_argo.wes.argo.api.service;

import static java.lang.String.format;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.icgc_argo.wes.argo.api.config.ElasticsearchProperties;
import org.icgc_argo.wes.argo.api.exceptions.NotFoundException;
import org.icgc_argo.wes.argo.api.graphql.model.Donor;
import org.icgc_argo.wes.argo.api.index.model.FileCentricDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class ArgoService {

  private final RestHighLevelClient client;
  private final String maestroIndex;
  private final int DEFAULT_HIT_SIZE = 100;

  @Autowired
  public ArgoService(
      @NonNull RestHighLevelClient client,
      @NonNull ElasticsearchProperties elasticsearchProperties) {
    this.client = client;
    this.maestroIndex = elasticsearchProperties.getMaestroIndex();
  }

  public Optional<FileCentricDocument> getFileCentricDocumentByAnalysisId(@NonNull String id) {
    try {
      val search = getDocByAnalysisIdAsJson(id);

      val customMapper =
          new ObjectMapper()
              .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
              .registerModule(new JavaTimeModule())
              .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
              .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      val doc = customMapper.readValue(search, FileCentricDocument.class);

      return Optional.of(doc);
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  public Optional<FileCentricDocument> getFileCentricDocumentByDonorId(@NonNull String donorId){
    try {
      val search = getDocByDonorIdAsJson(donorId);

      val customMapper =
              new ObjectMapper()
                      .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                      .registerModule(new JavaTimeModule())
                      .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      val doc = customMapper.readValue(search, FileCentricDocument.class);

      return Optional.of(doc);
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  private String getDocByAnalysisIdAsJson(@NonNull String analysisId) {
    val hit = searchByAnalysisId(analysisId);
    val search = hit.getSourceAsString();
    return search;
  }

  private String getDocByDonorIdAsJson(@NonNull String donorId){
    val hit = searchByDonorId(donorId);
    val search = hit.getSourceAsString();
    return search;
  }

  private SearchHit searchByAnalysisId(@NonNull String id) {
    try {
      val searchSourceBuilder = new SearchSourceBuilder();
      searchSourceBuilder.query(QueryBuilders.termQuery("analysis.id", id)).size(DEFAULT_HIT_SIZE);
      val searchResponse = search(searchSourceBuilder, maestroIndex);
      val hits = searchResponse.getHits().getHits();

      NotFoundException.checkNotFound(
          hits != null && hits.length > 0, format("Cannot find file centric document with analysis id = %s", id));

      return hits[0];
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  private SearchHit searchByDonorId(@NonNull String donorId) {
    try {
      val searchSourceBuilder = new SearchSourceBuilder();
      searchSourceBuilder.query(
              QueryBuilders
                      .nestedQuery("donors",
                              QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("donors.id", donorId)),
                              ScoreMode.Avg))
              .size(DEFAULT_HIT_SIZE);
      val searchResponse = search(searchSourceBuilder, maestroIndex);
      val hits = searchResponse.getHits().getHits();

      NotFoundException.checkNotFound(
              hits != null && hits.length > 0, format("Cannot find file centric document with donor id = %s", donorId));

      return hits[0];
    } catch (NotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }


  private SearchResponse search(@NonNull SearchSourceBuilder builder, @NonNull String index) {
    try {
      SearchRequest searchRequest = new SearchRequest(index);
      searchRequest.source(builder);
      val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
      return searchResponse;
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}
