package com.eLibrary.services;

import com.eLibrary.data.model.Book;
import com.eLibrary.data.model.Liberian;
import com.eLibrary.dtos.request.*;
import com.eLibrary.dtos.response.*;
import com.eLibrary.exception.ElibraryException;

import java.io.IOException;
import java.util.List;

public interface LiberianService {
    LiberianRegisterResponse register(LiberianRegisterRequest request) throws ElibraryException;

    LoginResponse login(LoginRequest request) throws ElibraryException;

    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) throws ElibraryException;

    SearchBookResponse searchBook(long id, SearchBookRequest request) throws ElibraryException, IOException;
    ReadingListResponse getReadingList(ReadingListRequest request) throws ElibraryException;

}
