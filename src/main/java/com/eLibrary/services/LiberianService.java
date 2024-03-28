package com.eLibrary.services;

import com.eLibrary.data.model.Book;
import com.eLibrary.data.model.Liberian;
import com.eLibrary.dtos.request.LiberianRegisterRequest;
import com.eLibrary.dtos.request.LoginRequest;
import com.eLibrary.dtos.request.ReadingListRequest;
import com.eLibrary.dtos.request.SearchBookRequest;
import com.eLibrary.dtos.response.LiberianRegisterResponse;
import com.eLibrary.dtos.response.LoginResponse;
import com.eLibrary.dtos.response.ReadingListResponse;
import com.eLibrary.dtos.response.SearchBookResponse;
import com.eLibrary.exception.ElibraryException;

import java.io.IOException;
import java.util.List;

public interface LiberianService {
    LiberianRegisterResponse register(LiberianRegisterRequest request) throws ElibraryException;

    LoginResponse login(LoginRequest request) throws ElibraryException;

    SearchBookResponse searchBook(long id, SearchBookRequest request) throws ElibraryException, IOException;
    ReadingListResponse getReadingList(ReadingListRequest request) throws ElibraryException;
}
