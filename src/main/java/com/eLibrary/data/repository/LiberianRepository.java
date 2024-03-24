package com.eLibrary.data.repository;

import com.eLibrary.data.model.Liberian;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LiberianRepository extends JpaRepository<Liberian,Long> {

}
