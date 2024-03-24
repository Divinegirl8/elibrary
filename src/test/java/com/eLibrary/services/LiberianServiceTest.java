package com.eLibrary.services;

import com.eLibrary.dtos.request.LiberianRegisterRequest;
import com.eLibrary.dtos.request.SearchBookRequest;
import com.eLibrary.dtos.response.LiberianRegisterResponse;
import com.eLibrary.dtos.response.SearchBookResponse;
import com.eLibrary.exception.ElibraryException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LiberianServiceTest {
   @Autowired
    LiberianService liberianService;

   @Test void testThatALiberianCanRegister(){
       LiberianRegisterRequest request = new LiberianRegisterRequest();
       request.setUsername("James");
       LiberianRegisterResponse response = liberianService.register(request);
       assertThat(response).isNotNull();
   }
    @Test void testThatALiberianCanRegister2(){
        LiberianRegisterRequest request = new LiberianRegisterRequest();
        request.setUsername("James");
        LiberianRegisterResponse response = liberianService.register(request);
        assertThat(response).isNotNull();
    }

   @Test void testThatALiberianCanSearchForBook() throws ElibraryException, IOException {
       SearchBookRequest request = new SearchBookRequest();
       request.setTitle("Romeo and Juliet");
    SearchBookResponse response = liberianService.searchBook(1,request);
      assertThat(response).isNotNull();

   }

}