package com.eLibrary.services;

import com.eLibrary.data.model.Book;
import com.eLibrary.data.model.Liberian;
import com.eLibrary.data.repository.LiberianRepository;
import com.eLibrary.dtos.request.LiberianRegisterRequest;
import com.eLibrary.dtos.request.LoginRequest;
import com.eLibrary.dtos.request.ReadingListRequest;
import com.eLibrary.dtos.request.SearchBookRequest;
import com.eLibrary.dtos.response.LiberianRegisterResponse;
import com.eLibrary.dtos.response.SearchBookResponse;
import com.eLibrary.exception.ElibraryException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class LiberianServiceTest {
   @Autowired
    LiberianService liberianService;
   @Autowired
    LiberianRepository liberianRepository;

   @Test void testThatALiberianCanRegister() throws ElibraryException {
       LiberianRegisterRequest request = new LiberianRegisterRequest();
       request.setUsername("James");
       request.setPassword("password");
       LiberianRegisterResponse response = liberianService.register(request);
       assertThat(response).isNotNull();
   }
    @Test void testThatALiberianCanRegister2() throws ElibraryException {
        LiberianRegisterRequest request = new LiberianRegisterRequest();
        request.setUsername("Derick");
        request.setPassword("password");
        LiberianRegisterResponse response = liberianService.register(request);
        log.info("register response -> {}",response);
        assertThat(response).isNotNull();
    }

   @Test void testThatALiberianCanSearchForBook() throws ElibraryException, IOException {
       SearchBookRequest request = new SearchBookRequest();
       request.setTitle("Romeo and Juliet");
    SearchBookResponse response = liberianService.searchBook(1,request);
      assertThat(response).isNotNull();

   }

    @Test void testThatALiberianCanSearchForBook2()  {
        SearchBookRequest request = new SearchBookRequest();
        request.setTitle("prideand");
        assertThrows(ElibraryException.class,()->liberianService.searchBook(2,request));

    }


    @Test
    void testThatALiberianCanSearchForBook3() throws ElibraryException, IOException {

        LiberianRegisterRequest registerRequest = new LiberianRegisterRequest();
        registerRequest.setUsername("Faith");
        registerRequest.setPassword("password");
        LiberianRegisterResponse registerResponse = liberianService.register(registerRequest);
        assertThat(registerResponse).isNotNull();


        SearchBookRequest searchRequest = new SearchBookRequest();
        searchRequest.setTitle("Romeo and Juliet");
        SearchBookResponse searchResponse = liberianService.searchBook(registerResponse.getId(), searchRequest);
        assertThat(searchResponse).isNotNull();
        log.info("response -> {}",searchResponse);

        SearchBookRequest searchRequest2 = new SearchBookRequest();
        searchRequest2.setTitle("Pride and Prejudice");
        SearchBookResponse searchResponse2 = liberianService.searchBook(registerResponse.getId(), searchRequest2);
        assertThat(searchResponse2).isNotNull();


        ReadingListRequest request = new ReadingListRequest();
        request.setId(registerResponse.getId());
        List<Book> readingList = liberianService.getReadingList(request);
        assertThat(Collections.singletonList(readingList)).isNotEmpty();


        log.info("reading-list -> {}", readingList);
    }

    @Test void aLiberianCanLogin() throws ElibraryException {
        LoginRequest request = new LoginRequest();
        request.setUsername("Faith");
        request.setPassword("password");
        liberianService.login(request);
        assertTrue(liberianRepository.findLiberianByUsername("Faith").isLogin());
    }


}