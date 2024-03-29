package com.eLibrary.controller;

import com.eLibrary.data.model.Book;
import com.eLibrary.dtos.request.*;
import com.eLibrary.dtos.response.*;
import com.eLibrary.exception.ElibraryException;
import com.eLibrary.services.LiberianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/api/v1")
public class LiberianController {
    @Autowired
    LiberianService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LiberianRegisterRequest liberianRegisterRequest){
        try{
        LiberianRegisterResponse response = service.register(liberianRegisterRequest);
        return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            LoginResponse response = service.login(loginRequest);
            return ResponseEntity.ok().body(response);
        } catch (ElibraryException e) {
          return ResponseEntity.status(UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request){
        try {
            ForgotPasswordResponse response = service.forgotPassword(request);
            return ResponseEntity.ok().body(response);
        } catch (ElibraryException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/searchBook/{id}")
    public ResponseEntity<?> search(@RequestBody SearchBookRequest request, @PathVariable("id") Long id){
        try {
            SearchBookResponse response = service.searchBook(id,request);
            return ResponseEntity.ok().body(response);

        }catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/readingList")
    public ResponseEntity<?> readingList(@RequestBody ReadingListRequest request){
        try {
            ReadingListResponse readingList = service.getReadingList(request);
            return ResponseEntity.ok().body(readingList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
