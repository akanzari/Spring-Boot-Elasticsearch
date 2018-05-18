package com.journaldev.elasticsearch.dao;

import java.util.Map;

public interface GenericRepository<E, K> {

  Map<String, Object> get(final K id);

  E save(final E entity);

  Map<String, Object> update(K id, E entity);

  void remove(final K id);

}