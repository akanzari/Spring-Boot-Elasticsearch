package com.journaldev.elasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfiguration extends AbstractFactoryBean<RestHighLevelClient> {

  private RestHighLevelClient restHighLevelClient;

  @Override
  public void destroy() {
    try {
      if (restHighLevelClient != null) {
        restHighLevelClient.close();
      }
    } catch (final Exception e) {
      logger.error("Error closing ElasticSearch client: ", e);
    }
  }

  @Override
  public Class<RestHighLevelClient> getObjectType() {
    return RestHighLevelClient.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }

  @Override
  public RestHighLevelClient createInstance() {
    return buildClient();
  }

  private RestHighLevelClient buildClient() {
    try {
      restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    } catch (Exception e) {
      logger.error("Error" + e);
    }
    return restHighLevelClient;
  }

}