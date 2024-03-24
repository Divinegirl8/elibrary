package com.eLibrary.data.repository;

import com.eLibrary.data.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
}
