package com.eLibrary.data.repository;

import com.eLibrary.data.model.Book;
import com.eLibrary.data.model.Liberian;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LiberianRepository extends JpaRepository<Liberian,Long> {
    Liberian findLiberianByUsername(String username);
}
