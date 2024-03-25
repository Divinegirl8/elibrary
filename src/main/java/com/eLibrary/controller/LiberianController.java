package com.eLibrary.controller;

import com.eLibrary.data.model.Book;
import com.eLibrary.dtos.request.LiberianRegisterRequest;
import com.eLibrary.dtos.request.ReadingListRequest;
import com.eLibrary.dtos.request.SearchBookRequest;
import com.eLibrary.dtos.response.LiberianRegisterResponse;
import com.eLibrary.dtos.response.SearchBookResponse;
import com.eLibrary.exception.ElibraryException;
import com.eLibrary.services.LiberianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/searchBook/{id}")
    public ResponseEntity<?> search(@RequestBody SearchBookRequest request, @PathVariable("id") Long id){
        try {
            SearchBookResponse response = service.searchBook(id,request);
            return ResponseEntity.ok().body(response);

        }catch (Exception e){
         return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/readingList")
    public ResponseEntity<?> readingList(@RequestBody ReadingListRequest request){
        try {
            List<Book> readingList = service.getReadingList(request);
            return ResponseEntity.ok().body(readingList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
