package com.eLibrary.data.repository;

import com.eLibrary.data.model.Liberian;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class LiberianRepositoryTest {
    @Autowired
    LiberianRepository liberianRepository;

    @Test
    @Sql(scripts = {"/scripts/insert.sql"})
    public void testThatUserExistInTheDatabase(){
        Optional<Liberian> liberian =liberianRepository.findById(100L);
        assertThat(liberian).isNotNull();

        log.info("liberian -> {}",liberian);
    }



}