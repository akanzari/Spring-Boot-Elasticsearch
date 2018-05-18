package com.journaldev.elasticsearch.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.journaldev.elasticsearch.bean.Book;
import com.journaldev.elasticsearch.dao.BookRepositorySearch;
import com.journaldev.elasticsearch.dao.jpa.BookRepository;

@Service
public class BookServiceImpl implements BookService {

  private final BookRepositorySearch bookRepositorySearch;

  private final BookRepository bookRepository;

  @Autowired
  public BookServiceImpl(BookRepositorySearch bookRepositorySearch, BookRepository bookRepository) {
    this.bookRepositorySearch = bookRepositorySearch;
    this.bookRepository = bookRepository;
  }

  @Override
  public Map<String, Object> get(Long id) {
    return bookRepositorySearch.get(id);
  }

  @Override
  public Book save(Book entity) {
    bookRepository.save(entity);
    return bookRepositorySearch.save(entity);
  }

  @Override
  public Map<String, Object> update(Long id, Book entity) {
    return bookRepositorySearch.update(id, entity);
  }

  @Override
  public void remove(Long id) {
    bookRepository.delete(id);
    bookRepositorySearch.remove(id);
  }

}