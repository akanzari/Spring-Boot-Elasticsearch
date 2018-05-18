package com.journaldev.elasticsearch.controller;

import com.journaldev.elasticsearch.bean.Book;
import com.journaldev.elasticsearch.service.BookService;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @PostMapping
  public Book insertBook(@RequestBody Book book) {
    return bookService.save(book);
  }

  @GetMapping("/{id}")
  public Map<String, Object> getBookById(@PathVariable Long id) throws IOException {
    return bookService.get(id);
  }

  @PutMapping("/{id}")
  public Map<String, Object> updateBookById(@RequestBody Book book, @PathVariable Long id) throws IOException {
    return bookService.update(id, book);
  }

  @DeleteMapping("/{id}")
  public void deleteBookById(@PathVariable Long id) throws IOException {
    bookService.remove(id);
  }

}