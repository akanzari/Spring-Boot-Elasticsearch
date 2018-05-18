package com.journaldev.elasticsearch.dao;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericRepositoryImpl<E, K extends Serializable> implements GenericRepository<E, K> {

  private static Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);

  private Class<E> clazz;

  private final String index = clazz.getSimpleName().toLowerCase();

  private RestHighLevelClient restHighLevelClient;

  private ObjectMapper objectMapper;

  public GenericRepositoryImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
    this.objectMapper = objectMapper;
    this.restHighLevelClient = restHighLevelClient;
  }

  @Override
  public Map<String, Object> get(K id) {
    GetRequest getRequest = new GetRequest(index, index, (String) id);
    Map<String, Object> sourceAsMap = null;
    try {
      GetResponse getResponse = restHighLevelClient.get(getRequest);
      if (getResponse != null) {
        sourceAsMap = getResponse.getSourceAsMap();
      }
    } catch (IOException e) {
      logger.error("Error" + e);
    }
    return sourceAsMap;
  }

  @SuppressWarnings("unchecked")
  @Override
  public E save(E entity) {
    Map<String, Object> dataMap = objectMapper.convertValue(entity, Map.class);
    String id = null;
    try {
      id = String.valueOf(getField(entity.getClass(), "id").get(entity));
    } catch (IllegalAccessException e) {
      logger.error("Error" + e);
    }
    IndexRequest indexRequest = new IndexRequest(index, index, id).source(dataMap);
    try {
      if (indexRequest != null) {
        restHighLevelClient.index(indexRequest);
      }
    } catch (IOException ex) {
      logger.error("Error" + ex);
    }
    return entity;
  }

  @Override
  public Map<String, Object> update(K id, E entity) {
    UpdateRequest updateRequest = new UpdateRequest(index, index, (String) id).fetchSource(true);
    Map<String, Object> error = new HashMap<String, Object>();
    error.put("Error", "Unable to update book");
    Map<String, Object> sourceAsMap = null;
    try {
      String bookJson = objectMapper.writeValueAsString(entity);
      updateRequest.doc(bookJson, XContentType.JSON);
      UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
      if (updateResponse != null) {
        sourceAsMap = updateResponse.getGetResult().sourceAsMap();
      }
    } catch (IOException e) {
      logger.error("Error" + e);
    }
    return sourceAsMap;
  }

  @Override
  public void remove(K id) {
    DeleteRequest deleteRequest = new DeleteRequest(index, index, (String) id);
    try {
      restHighLevelClient.delete(deleteRequest);
    } catch (IOException e) {
      logger.error("Error" + e);
    }
  }

  private static Field getField(Class<?> clazz, String fieldName) {
    Class<?> tmpClass = clazz;
    do {
      for (Field field : tmpClass.getDeclaredFields()) {
        String candidateName = field.getName();
        if (!candidateName.equals(fieldName)) {
          continue;
        }
        field.setAccessible(true);
        return field;
      }
      tmpClass = tmpClass.getSuperclass();
    } while (clazz != null);
    throw new IllegalArgumentException("Field '" + fieldName + "' not found on class " + clazz);
  }

}