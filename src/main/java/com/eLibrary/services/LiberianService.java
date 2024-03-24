package com.eLibrary.services;

import com.eLibrary.dtos.request.LiberianRegisterRequest;
import com.eLibrary.dtos.request.SearchBookRequest;
import com.eLibrary.dtos.response.LiberianRegisterResponse;
import com.eLibrary.dtos.response.SearchBookResponse;
import com.eLibrary.exception.ElibraryException;

import java.io.IOException;

public interface LiberianService {
LiberianRegisterResponse register(LiberianRegisterRequest request);

    SearchBookResponse searchBook(long id, SearchBookRequest request) throws ElibraryException, IOException;
}
