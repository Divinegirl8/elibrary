package com.eLibrary.services;

import com.eLibrary.data.model.Book;
import com.eLibrary.data.model.Liberian;
import com.eLibrary.data.repository.LiberianRepository;
import com.eLibrary.dtos.request.*;
import com.eLibrary.dtos.response.*;
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
       request.setUsername("james1");
       request.setPassword("password1+");
       LiberianRegisterResponse response = liberianService.register(request);
       assertThat(response).isNotNull();
   }
    @Test void testThatALiberianCanRegister2() throws ElibraryException {
        LiberianRegisterRequest request = new LiberianRegisterRequest();
        request.setUsername("derick0");
        request.setPassword("password=0");
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
        registerRequest.setUsername("faith1");
        registerRequest.setPassword("password8-");
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
        ReadingListResponse readingList = liberianService.getReadingList(request);
        assertThat(Collections.singletonList(readingList)).isNotEmpty();


        log.info("reading-list -> {}", readingList);
    }

    @Test void aLiberianCanLogin() throws ElibraryException {
        LoginRequest request = new LoginRequest();
        request.setUsername("faith1");
        request.setPassword("password8-");
       LoginResponse response = liberianService.login(request);
       assertThat(response).isNotNull();
    }

    @Test void aLiberianCanChangePassword() throws ElibraryException {
        ForgotPasswordRequest  request = new ForgotPasswordRequest();
        request.setUsername("faith1");
        request.setPassword("pass1=8");

        ForgotPasswordResponse response = liberianService.forgotPassword(request);
        assertThat(response).isNotNull();
    }


}