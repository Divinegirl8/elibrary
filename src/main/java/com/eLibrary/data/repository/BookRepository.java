package com.eLibrary.data.repository;

import com.eLibrary.data.model.Book;
import com.eLibrary.data.model.Liberian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

}
