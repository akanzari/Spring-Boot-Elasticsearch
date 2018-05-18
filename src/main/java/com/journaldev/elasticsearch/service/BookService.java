package com.journaldev.elasticsearch.service;

import java.util.Map;

import com.journaldev.elasticsearch.bean.Book;

public interface BookService {

  Map<String, Object> get(final Long id);

  Book save(final Book entity);

  Map<String, Object> update(Long id, Book entity);

  void remove(final Long id);

}