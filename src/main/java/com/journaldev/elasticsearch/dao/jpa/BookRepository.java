package com.journaldev.elasticsearch.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.journaldev.elasticsearch.bean.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}